package progettotlp.rest.application;

import java.util.Arrays;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import progettotlp.rest.beans.ErrorBean;
import progettotlp.rest.beans.ResponseBean;
import progettotlp.ui.AbstractFormUtilities;

@Provider
public class GenericJerseyExceptionMapper implements ExceptionMapper<Throwable> {

	private Logger logger = LoggerFactory.getLogger(GenericJerseyExceptionMapper.class);

    @Override
    public Response toResponse(Throwable t) {
    	logger.error("Error", t);
        ResponseBean bean = new ResponseBean();
        ErrorBean ebean = new ErrorBean();
        ebean.setErrorUserMsg(t.getMessage());
        ebean.setErrorUserTitle(t.getClass().getCanonicalName());
        ebean.setType(t.getClass().getCanonicalName());
        ebean.setCode(Long.valueOf(Status.INTERNAL_SERVER_ERROR.getStatusCode()));
        bean.setError(Arrays.asList(ebean));
        return Response.ok(bean, MediaType.APPLICATION_JSON).build();
    }
}
