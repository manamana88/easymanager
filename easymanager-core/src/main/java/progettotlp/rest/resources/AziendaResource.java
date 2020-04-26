package progettotlp.rest.resources;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import progettotlp.classes.Azienda;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.ValidationException;
import progettotlp.facilities.BeanUtils;
import progettotlp.facilities.Controlli;
import progettotlp.interfaces.AziendaInterface;
import progettotlp.persistenza.AziendaManager;
import progettotlp.persistenza.ManagerProvider;

@Path("azienda")
public class AziendaResource {
	
	private AziendaManager aziendaManager = ManagerProvider.getAziendaManager();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@QueryParam("id") String id){
		AziendaInterface azienda;
		if ("PRINCIPALE".equalsIgnoreCase(id)){
			azienda = aziendaManager.getAziendaPrincipale();
		} else {
			azienda = aziendaManager.get(Azienda.class, Long.parseLong(id));
		}
		return Response.ok(BeanUtils.createResponseBean(azienda), MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(){
		List<Azienda> aziendeNonPrincipali = aziendaManager.getAziendeNonPrincipali();
		return Response.ok(BeanUtils.createResponseBean(aziendeNonPrincipali.toArray()), MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveAzienda(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			Azienda res
			) throws ValidationException, PersistenzaException{
        checkAzienda(res);
        aziendaManager.registraAzienda(res);
        return Response.ok().build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response editAzienda(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			Azienda res) throws ValidationException, PersistenzaException, GenericExceptionToPrint{
		if (res.getId()==null){
			throw new GenericExceptionToPrint("Dati errati", "Siamo spiacenti, questa azienda non \u00E8 registrata.");
		}
		checkAzienda(res);
        try{
            aziendaManager.modificaAzienda(res);
        } catch (Exception e){
            throw new GenericExceptionToPrint("Errore salvataggio", "Errore salvataggio azienda, riprovare",e);
        }
		return Response.ok().build();
	}
	
	@DELETE
	public Response deleteAzienda(
			@QueryParam("id") Long id
			) throws PersistenzaException{
		AziendaInterface a = aziendaManager.get(Azienda.class, id);
		aziendaManager.cancellaAzienda(a);
		return Response.ok().build();
	}

    protected void checkAzienda(AziendaInterface a) throws ValidationException{
        String nome = a.getNome();
        if (nome==null || nome.trim().isEmpty()) {
            throw new ValidationException("Campo vuoto", "Il nome dell'azienda \u00E8 necessario");
        }
        if (!Controlli.checkIva(a.getPIva(), true)) {
            throw new ValidationException("Dati errati", "Partita IVA errata");
        }
        if (!Controlli.checkMail(a.getMail(), false)){
            throw new ValidationException("E-mail errata", "Inserire una mail valida");
        }
        String pec = a.getPEC();
		if (!Controlli.checkMail(pec, false)){
        	throw new ValidationException("PEC errata", "Inserire una PEC valida");
        }
        String codiceFatturaPa = a.getCodiceFatturaPa();
		if (!Controlli.checkCodiceFatturaPa(codiceFatturaPa, false)){
        	throw new ValidationException("Codice Fattura PA errata", "Inserire un Codice Fattura PA valido. Deve essere un codice di 6 o 7 cifre");
        }
        if ((pec==null || pec.trim().isEmpty()) && (codiceFatturaPa==null || codiceFatturaPa.trim().isEmpty())){
        	throw new ValidationException("Campi obbligatori", "Inserire almeno uno fra PEC e Codice Fattura PA");
        }
        if (!Controlli.checkCodFIS(a.getCodFis(), true)){
            throw new ValidationException("Dati errati", "Il campo Codice Fiscale contiene dei dati errati");
        }
        boolean registrazioneEmpty = isRegistrazioneEmpty(a);
		if (a.getTassabile() && !registrazioneEmpty){
			throw new ValidationException("Dati errati", "I campi relativi ad autorizzazione e registrazione non sono vuoti");
        }
		if (!a.getTassabile() && registrazioneEmpty){
			throw new ValidationException("Dati errati", "I campi relativi ad autorizzazione e registrazione sono vuoti");
		}
    }

	private boolean isRegistrazioneEmpty(AziendaInterface a) {
		String numeroAutorizzazione = a.getNumeroAutorizzazione();
		if (numeroAutorizzazione!=null && !numeroAutorizzazione.trim().isEmpty()){
			return false;
		} else {
			String numeroRegistrazione = a.getNumeroRegistrazione();
			if (numeroRegistrazione !=null && !numeroRegistrazione.trim().isEmpty()){
				return false;
			} else {
				return a.getDataAutorizzazione() == null && a.getDataRegistrazione()==null;
			}
		}
	}
}
