package progettotlp.rest.resources;

import java.io.File;
import java.io.FileNotFoundException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/filesystem{objectPath: .*}")
public class FilesystemResource {

	@PathParam("objectPath")
	private String objectPathString;
	
	@GET
	public Response getResource() throws FileNotFoundException{
		File file = new File(objectPathString);
		if (!file.exists()){
			throw new FileNotFoundException("Cannot find file with path: ["+objectPathString+"]");
		}
		String mediaType;
		if (file.getName().endsWith(".pdf")){
			mediaType = "application/pdf";
		} else {
			mediaType = MediaType.TEXT_HTML;
		}
		return Response.ok(file,mediaType).build();
	}
}
