package progettotlp.rest.resources;

import java.text.ParseException;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import progettotlp.facilities.BeanUtils;
import progettotlp.facilities.ConfigurationManager;
import progettotlp.facilities.DateUtils;

@Path("utils")
public class UtilsResource {

	@GET
	@Path("calculateScadenza")
	public Response recalculateScadenza(
			@QueryParam("date") String date,
			@QueryParam("giorni") int giorni) throws ParseException{
		Date parseDate = DateUtils.parseDate(date);
		Date calcolaScadenza = DateUtils.calcolaScadenza(parseDate, giorni);
		return Response.ok(BeanUtils.createResponseBean(calcolaScadenza), MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@GET
	@Path("getProperty")
	public Response recalculateScadenza(
			@QueryParam("name") String name) throws ParseException{
		return Response.ok(BeanUtils.createResponseBean(ConfigurationManager.getProperty(name)), MediaType.APPLICATION_JSON_TYPE).build();
	}
}
