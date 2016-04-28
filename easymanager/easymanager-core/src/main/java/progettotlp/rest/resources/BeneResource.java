package progettotlp.rest.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import progettotlp.classes.Bene;
import progettotlp.facilities.BeanUtils;
import progettotlp.persistenza.FatturaManager;
import progettotlp.persistenza.LastSameBeneFatturatoInfos;
import progettotlp.persistenza.ManagerProvider;

@Path("bene")
public class BeneResource {
	
	private FatturaManager fatturaManager = ManagerProvider.getFatturaManager();
	
	@POST
	@Path("/alreadyBilled")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(Bene b){
		LastSameBeneFatturatoInfos lastSameBeneFatturatoInfos = fatturaManager.getLastSameBeneFatturatoInfos(b);
		return Response.ok(BeanUtils.createResponseBean(lastSameBeneFatturatoInfos), MediaType.APPLICATION_JSON_TYPE).build();
	}
	
}