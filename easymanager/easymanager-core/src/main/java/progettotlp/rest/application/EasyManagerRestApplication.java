package progettotlp.rest.application;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("resources")
public class EasyManagerRestApplication extends ResourceConfig 
{
	
	public EasyManagerRestApplication() {
        packages("progettotlp.rest");
    }
}
