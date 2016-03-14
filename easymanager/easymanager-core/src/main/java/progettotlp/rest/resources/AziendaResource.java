package progettotlp.rest.resources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import progettotlp.classes.Azienda;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.ValidationException;
import progettotlp.facilities.Controlli;
import progettotlp.persistenza.AziendaManager;
import progettotlp.persistenza.AziendaManagerImpl;

@Path("azienda")
public class AziendaResource {
	
	private static AziendaManager aziendaManager = new AziendaManagerImpl();
	
	@POST
	public Response saveAzienda(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@FormParam("id") Long id,
			@FormParam("nome") String nome, 
			@FormParam("piva") String piva, 
			@FormParam("codFis") String codFis, 
			@FormParam("via") String via, 
			@FormParam("civico") String civico, 
			@FormParam("cap") String cap, 
			@FormParam("citta") String citta, 
			@FormParam("provincia") String provincia, 
			@FormParam("nazione") String nazione, 
			@FormParam("mail") String mail, 
			@FormParam("telefono") String telefono, 
			@FormParam("fax") String fax, 
			@FormParam("tassabile") boolean tassabile,
			@FormParam("principale") @DefaultValue("false") boolean principale
			) throws ValidationException, PersistenzaException{
		Azienda res=new Azienda();
        res.setId(id);
        res.setPrincipale(principale);
        res.setNome(nome);
        res.setPIva(piva);
        res.setCodFis(codFis);
        res.setMail(mail);
        res.setVia(via);
        res.setCivico(civico);
        res.setCap(cap);
        res.setCitta(citta);
        res.setProvincia(provincia);
        res.setNazione(nazione);
        res.setTelefono(telefono);
        res.setFax(fax);
        res.setTassabile(tassabile);
        checkAzienda(res);
        aziendaManager.registraAzienda(res);
        return Response.ok().build();
	}

	@PUT
	public Response editAzienda(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@FormParam("id") Long id,
			@FormParam("nome") String nome, 
			@FormParam("piva") String piva, 
			@FormParam("codFis") String codFis, 
			@FormParam("via") String via, 
			@FormParam("civico") String civico, 
			@FormParam("cap") String cap, 
			@FormParam("citta") String citta, 
			@FormParam("provincia") String provincia, 
			@FormParam("nazione") String nazione, 
			@FormParam("mail") String mail, 
			@FormParam("telefono") String telefono, 
			@FormParam("fax") String fax, 
			@FormParam("tassabile") boolean tassabile,
			@FormParam("principale") @DefaultValue("false") boolean principale
			) throws ValidationException, PersistenzaException, GenericExceptionToPrint{
		if (id==null){
			throw new GenericExceptionToPrint("Dati errati", "Siamo spiacenti, questa azienda non è registrata.");
		}
		Azienda res=new Azienda();
		res.setId(id);
		res.setPrincipale(principale);
		res.setNome(nome);
		res.setPIva(piva);
		res.setCodFis(codFis);
		res.setMail(mail);
		res.setVia(via);
		res.setCivico(civico);
		res.setCap(cap);
		res.setCitta(citta);
		res.setProvincia(provincia);
		res.setNazione(nazione);
		res.setTelefono(telefono);
		res.setFax(fax);
		res.setTassabile(tassabile);
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
		Azienda a = aziendaManager.get(Azienda.class, id);
		aziendaManager.cancellaAzienda(a);
		return Response.ok().build();
	}

    protected void checkAzienda(Azienda a) throws ValidationException{
        String nome = a.getNome();
        if (nome==null || nome.trim().isEmpty()) {
            throw new ValidationException("Campo vuoto", "Il nome dell'azienda è necessario");
        }
        if (!Controlli.checkIva(a.getpIva(), true)) {
            throw new ValidationException("Dati errati", "Partita IVA errata");
        }
        if (!Controlli.checkMail(a.getMail(), false)){
            throw new ValidationException("E-mail errata", "Inserire una mail valida");
        }
        if (!Controlli.checkCodFIS(a.getCodFis(), true)){
            throw new ValidationException("Dati errati", "Il campo Codice Fiscale contiene dei dati errati");
        }
    }
}
