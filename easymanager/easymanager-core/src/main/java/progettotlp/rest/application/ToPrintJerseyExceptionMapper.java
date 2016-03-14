package progettotlp.rest.application;

import java.util.Arrays;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import progettotlp.exceptions.toprint.AbstractExceptionToPrint;
import progettotlp.rest.beans.ErrorBean;
import progettotlp.rest.beans.ResponseBean;

@Provider
public class ToPrintJerseyExceptionMapper implements ExceptionMapper<AbstractExceptionToPrint> {

	private Logger logger = LoggerFactory.getLogger(ToPrintJerseyExceptionMapper.class);
	
    @Override
    public Response toResponse(AbstractExceptionToPrint t) {
    	logger.error("Error", t);
        ResponseBean bean = new ResponseBean();
        ErrorBean ebean = new ErrorBean();
        ebean.setErrorUserMsg(t.getBody());
        ebean.setErrorUserTitle(t.getHeader());
        ebean.setType(t.getClass().getCanonicalName());
        ebean.setCode(Long.valueOf(Status.INTERNAL_SERVER_ERROR.getStatusCode()));
        bean.setError(Arrays.asList(ebean));
        return Response.ok(bean, MediaType.APPLICATION_JSON).build();
    }
}
