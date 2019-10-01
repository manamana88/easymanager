package progettotlp.rest.resources;

import java.text.ParseException;
import java.util.Date;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import progettotlp.Constants;
import progettotlp.facilities.BeanUtils;
import progettotlp.facilities.DateUtils;
import progettotlp.facilities.Utility;
import progettotlp.persistenza.ManagerProvider;
import progettotlp.persistenza.StatisticheManager;

@Path("anno")
public class AnnoResource {

	@GET
	@Path("/list")
	public Response getYears() throws ParseException{
		StatisticheManager statisticheManager = ManagerProvider.getStatisticheManager();
		Set<Integer> availableYears = statisticheManager.getAvailableYears();
		availableYears.add(DateUtils.getYear(new Date()));
		return Response.ok(BeanUtils.createResponseBean(availableYears.toArray()), MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@GET
	public Response get() throws ParseException{
		return Response.ok(BeanUtils.createResponseBean(Utility.getSelectedAnno()), MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@POST
	public Response post(@QueryParam("year") int year) throws ParseException{
		System.setProperty(Constants.CURRENT_YEAR_PROPERTY, year+"");
		return Response.ok().build();
	}
}
