package progettotlp.rest.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("logout")
public class LogoutResource {

	@GET
	public Response test(@Context HttpServletRequest request){
		request.getSession().invalidate();
		return Response.status(Status.UNAUTHORIZED).build();
	}
}
