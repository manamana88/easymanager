package progettotlp.rest.resources;

import static progettotlp.rest.utils.JsonUtils.getBooleanValue;
import static progettotlp.rest.utils.JsonUtils.getDateValue;
import static progettotlp.rest.utils.JsonUtils.getDoubleValue;
import static progettotlp.rest.utils.JsonUtils.getBigDecimalValue;
import static progettotlp.rest.utils.JsonUtils.getIntValue;
import static progettotlp.rest.utils.JsonUtils.getLongValue;
import static progettotlp.rest.utils.JsonUtils.getTextValue;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
import progettotlp.classes.Bene;
import progettotlp.classes.DdT;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.exceptions.toprint.CantRetrieveException;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.ValidationException;
import progettotlp.facilities.BeanUtils;
import progettotlp.facilities.DateUtils;
import progettotlp.interfaces.BeneInterface;
import progettotlp.interfaces.DdTInterface;
import progettotlp.persistenza.AziendaManager;
import progettotlp.persistenza.DdTManager;
import progettotlp.persistenza.ManagerProvider;
import progettotlp.print.DdtPrinter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Path("ddt")
public class DdtResource {

	private DdTManager ddtManager = ManagerProvider.getDdtManager();
	private AziendaManager aziendaManager = ManagerProvider.getAziendaManager();
	
	@GET
	@Path("next")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNextDdt() {
		int lastDdT = ddtManager.getLastDdT();
		int nextDdtNumber = lastDdT+1;
		String todayDate = DateUtils.formatDate(new Date());
		List<Azienda> aziende = aziendaManager.getAziendeNonPrincipali();
		return Response.ok(BeanUtils.createResponseBean(nextDdtNumber, todayDate, aziende), MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDdt(@QueryParam("id") Long id) {
		DdTInterface ddt = ddtManager.getDdT(id, true, true);
		return Response.ok(BeanUtils.createResponseBean(ddt), MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@GET
	@Path("print")
	@Produces(MediaType.APPLICATION_JSON)
	public Response printDdt(@QueryParam("id") Long id) throws Exception {
		DdTInterface toPrint=ddtManager.getDdT(id, true, false);
        if (toPrint!=null){
            try {
                File file = DdtPrinter.printPage(aziendaManager.getAziendaPrincipale(), toPrint, false);
                return Response.ok(BeanUtils.createResponseBean(file.getAbsolutePath()), MediaType.APPLICATION_JSON_TYPE).build();
            } catch (Exception ex) {
                throw new GenericExceptionToPrint("Errore","Siamo spiacenti si \u00E8 verificato un errore.\nImpossibile stampare il DdT",ex);
            }
        } else{
            throw new CantRetrieveException("Errore","Siamo spiacenti si \u00E8 verificato un errore.\nImpossibile recuperare il DdT");
        }
	}
	
	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(
			@QueryParam("company") Long id) throws ParseException {
		List<DdT> allDdT;
		if (id!=null){
			allDdT = ddtManager.getAllDdT(id);
		} else {
			allDdT = ddtManager.getAllDdT(false, true);
		}
		for (DdTInterface ddt : allDdT) {
			ddt.setBeni(null);
		}
		return Response.ok(BeanUtils.createResponseBean(allDdT.toArray()), MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveDdt(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			ObjectNode ddt
			) throws ValidationException, PersistenzaException, ParseException{
		DdTInterface parsed = parseDdT(ddt);
		ddtManager.registraDdT(parsed);
        return Response.ok().build();
	}

	@PUT
	public Response editDdt(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			ObjectNode ddt
			) throws ValidationException, PersistenzaException, GenericExceptionToPrint, ParseException{
		DdTInterface parsed = parseDdT(ddt);
		ddtManager.modificaDdT(parsed);
		return Response.ok().build();
	}
	
	@DELETE
	public Response deleteDdt(
			@QueryParam("id") Long id
			) throws PersistenzaException{
		ddtManager.cancellaDdT(id);
		return Response.ok().build();
	}
	
	private DdTInterface parseDdT(ObjectNode ddt) throws ParseException, ValidationException {
		DdTInterface ddtObject = new DdT();
		ddtObject.setAnnotazioni(getTextValue(ddt, "annotazioni"));
		ddtObject.setAspettoEsteriore(getTextValue(ddt, "aspettoEsteriore"));
		List<BeneInterface> beni = parseBeni((ArrayNode) ddt.get("beni"));
        if (beni.isEmpty()){
            throw new ValidationException("Dati errati", "Il ddt non contiene beni");
        }
		ddtObject.setBeni(beni);
		ddtObject.setCausale(getTextValue(ddt, "causale"));
		Azienda azienda = aziendaManager.get(Azienda.class, getLongValue(ddt, "cliente"));
		ddtObject.setCliente(azienda);
		ddtObject.setColli(getIntValue(ddt, "colli"));
		ddtObject.setData(getDateValue(ddt, "data"));
		ddtObject.setDestinazione(getTextValue(ddt, "destinazione"));
		ddtObject.setFatturabile(getBooleanValue(ddt, "fatturabile"));
		ddtObject.setId(getIntValue(ddt, "id"));
		ddtObject.setMezzo(getTextValue(ddt, "mezzo"));
		ddtObject.setPeso(getDoubleValue(ddt, "peso"));
		ddtObject.setPorto(getTextValue(ddt, "porto"));
		ddtObject.setProgressivo(getIntValue(ddt, "progressivo"));
		ddtObject.setRealId(getLongValue(ddt, "realId"));
		ddtObject.setRitiro(getTextValue(ddt, "ritiro"));
		ddtObject.setTipo(getTextValue(ddt, "tipo"));
		ddtObject.setVostroOrdine(getTextValue(ddt, "vostroOrdine"));
		ddtObject.setVostroOrdineDel(getTextValue(ddt, "vostroOrdineDel"));
		return ddtObject;
	}
	
	private List<BeneInterface> parseBeni(ArrayNode arrayNode) throws ValidationException {
		List<BeneInterface> result = new ArrayList<>();
		for (JsonNode jsonNode : arrayNode) {
			ObjectNode node = (ObjectNode) jsonNode;
			Bene bene = new Bene();
			bene.setCampionario(getBooleanValue(node, "campionario"));
			bene.setCodice(getTextValue(node, "codice"));
			bene.setCommessa(getTextValue(node, "commessa"));
			bene.setDescrizione(getTextValue(node, "descrizione"));
			bene.setId(getLongValue(node, "id"));
			bene.setInteramenteAdesivato(getBooleanValue(node, "interamenteAdesivato"));
			bene.setPiazzato(getBooleanValue(node, "piazzato"));
			bene.setPrezzo(getBigDecimalValue(node, "prezzo"));
			bene.setPrimoCapo(getBooleanValue(node, "primoCapo"));
			bene.setPrototipo(getBooleanValue(node, "prototipo"));
			bene.setQta(getBigDecimalValue(node, "qta"));
			bene.setTot(getBigDecimalValue(node, "totale"));
			validateBene(bene);
			result.add(bene);
		}
		return result;
	}

    protected void validateBene(BeneInterface bene) throws ValidationException {
        String codice = bene.getCodice();
        if (codice==null || codice.trim().isEmpty())
            throw new ValidationException("Dati errati", "Codice nullo");
        String descrizione = bene.getDescrizione();
        if (descrizione==null || descrizione.trim().isEmpty())
            throw new ValidationException("Dati errati", "Descrizione vuota");
        BigDecimal qta = bene.getQta();
        if (qta==null || qta.signum()<=0)
            throw new ValidationException("Dati errati", "Quantit\u00E1 vuota");
    }
}
