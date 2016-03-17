package progettotlp.rest.resources;

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
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.ValidationException;
import progettotlp.facilities.BeanUtils;
import progettotlp.facilities.DateUtils;
import progettotlp.persistenza.AziendaManager;
import progettotlp.persistenza.DdTManager;
import progettotlp.persistenza.ManagerProvider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Path("ddt")
public class DdtResource {
	
	private static DdTManager ddtManager = ManagerProvider.getDdtManager();
	private static AziendaManager aziendaManager = ManagerProvider.getAziendaManager();
	
	@GET
	@Path("next")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNextDdt() {
		Integer lastDdT = ddtManager.getLastDdT();
		if (lastDdT==null){
			lastDdT=0;
		}
		int nextDdtNumber = lastDdT+1;
		String todayDate = DateUtils.formatDate(new Date());
		List<Azienda> aziende = aziendaManager.getAziendeNonPrincipali();
		return Response.ok(BeanUtils.createResponseBean(nextDdtNumber, todayDate, aziende), MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDdt(@QueryParam("id") Long id) {
		DdT ddt = ddtManager.getDdT(id, true, true);
		return Response.ok(BeanUtils.createResponseBean(ddt), MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		List<DdT> allDdT = ddtManager.getAllDdT(false, false);
		for (DdT ddt : allDdT) {
			ddt.setBeni(null);
			ddt.setFattura(null);
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
		DdT parsed = parseDdT(ddt);
		ddtManager.registraDdT(parsed);
        return Response.ok().build();
	}

	@PUT
	public Response editDdt(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			ObjectNode ddt
			) throws ValidationException, PersistenzaException, GenericExceptionToPrint, ParseException{
		DdT parsed = parseDdT(ddt);
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
	
	private DdT parseDdT(ObjectNode ddt) throws ParseException, ValidationException {
		DdT ddtObject = new DdT();
		ddtObject.setAnnotazioni(getTextValue(ddt, "annotazioni"));
		ddtObject.setAspettoEsteriore(getTextValue(ddt, "aspettoEsteriore"));
		List<Bene> beni = parseBeni((ArrayNode) ddt.get("beni"));
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
	
	private List<Bene> parseBeni(ArrayNode arrayNode) throws ValidationException {
		List<Bene> result = new ArrayList<Bene>();
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
			bene.setPrezzo(getFloatValue(node, "prezzo"));
			bene.setPrimoCapo(getBooleanValue(node, "primoCapo"));
			bene.setPrototipo(getBooleanValue(node, "prototipo"));
			bene.setQta(getIntValue(node, "qta"));
			bene.setTot(getFloatValue(node, "totale"));
			validateBene(bene);
			result.add(bene);
		}
		return result;
	}

	private Date getDateValue(ObjectNode ddt, String fieldName) throws ParseException {
		String textValue = getTextValue(ddt, fieldName);
		return textValue != null ? DateUtils.parseDate(textValue) : null; 
	}
	
	private Float getFloatValue(ObjectNode ddt, String fieldName) {
		Number numberValue = getNumberValue(ddt, fieldName);
		return numberValue != null ? numberValue.floatValue() : null;
	}
	
	private Double getDoubleValue(ObjectNode ddt, String fieldName) {
		Number numberValue = getNumberValue(ddt, fieldName);
		return numberValue != null ? numberValue.doubleValue() : null;
	}
	
	private Long getLongValue(ObjectNode ddt, String fieldName) {
		Number numberValue = getNumberValue(ddt, fieldName);
		return numberValue != null ? numberValue.longValue() : null;
	}

	private Integer getIntValue(ObjectNode ddt, String fieldName) {
		Number numberValue = getNumberValue(ddt, fieldName);
		return numberValue != null ? numberValue.intValue() : null;
	}

	private Boolean getBooleanValue(ObjectNode ddt, String fieldName) {
		JsonNode jsonNode = ddt.get(fieldName);
		return jsonNode != null ? jsonNode.booleanValue() : null;
	}
	
	private Number getNumberValue(ObjectNode ddt, String fieldName) {
		JsonNode jsonNode = ddt.get(fieldName);
		if (jsonNode != null){
			return jsonNode.numberValue();
		}
		return null;
	}
	
	private String getTextValue(ObjectNode ddt, String fieldName) {
		JsonNode jsonNode = ddt.get(fieldName);
		if (jsonNode != null){
			return jsonNode.textValue();
		} else {
			return "";
		}
	}

    protected void validateBene(Bene bene) throws ValidationException {
        String codice = bene.getCodice();
        if (codice==null || codice.trim().isEmpty())
            throw new ValidationException("Dati errati", "Codice nullo");
        String descrizione = bene.getDescrizione();
        if (descrizione==null || descrizione.trim().isEmpty())
            throw new ValidationException("Dati errati", "Descrizione vuota");
        Integer qta = bene.getQta();
        if (qta==null || qta<=0)
            throw new ValidationException("Dati errati", "Quantità vuota");
    }
}
