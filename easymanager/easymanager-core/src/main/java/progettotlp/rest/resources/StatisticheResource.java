package progettotlp.rest.resources;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import progettotlp.persistenza.ManagerProvider;
import progettotlp.persistenza.StatisticheManager;
import progettotlp.statistiche.StatisticheConfronto;
import progettotlp.statistiche.StatisticheFattura;

@Path("statistiche")
public class StatisticheResource {

	private StatisticheManager statisticheManager = ManagerProvider.getStatisticheManager();
	
	@GET
	@Path("simple")
	@Produces(MediaType.APPLICATION_JSON)
	public Response simple(
			@QueryParam("startDate") Date startDate,
			@QueryParam("endDate") Date endDate, 
			@QueryParam("aziende") List<String> aziende) {
		Map<Date, List<StatisticheFattura>> simpleSearch = statisticheManager.simpleSearch(startDate, endDate, aziende);
		return Response.ok(simpleSearch).build();
	}
	
	@GET
	@Path("advanced")
	@Produces(MediaType.APPLICATION_JSON)
	public Response advanced(
			@QueryParam("startDate") Date startDate,
			@QueryParam("endDate") Date endDate, 
			@QueryParam("startDate2") Date startDate2,
			@QueryParam("endDate2") Date endDate2, 
			@QueryParam("aziende") List<String> aziende) {
		StatisticheConfronto advancedSearch = statisticheManager.advancedSearch(startDate, endDate, startDate2, endDate2, aziende);
		return Response.ok(advancedSearch).build();
	}
	
}
