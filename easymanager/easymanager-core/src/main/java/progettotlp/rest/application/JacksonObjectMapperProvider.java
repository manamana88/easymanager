package progettotlp.rest.application;

import java.text.SimpleDateFormat;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import progettotlp.facilities.DateUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

@Provider
public class JacksonObjectMapperProvider implements ContextResolver<ObjectMapper> {

    private static final ObjectMapper defaultObjectMapper = createDefaultMapper();

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return JacksonObjectMapperProvider.getInstance();
    }

    public static ObjectMapper getInstance() {
        return defaultObjectMapper;
    }

    private static ObjectMapper createDefaultMapper() {
        final ObjectMapper result = new ObjectMapper();
        result.configure(SerializationFeature.INDENT_OUTPUT, true);
        result.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JaxbAnnotationModule module = new JaxbAnnotationModule();
        result.registerModule(module);
        result.setSerializationInclusion(Include.NON_NULL);
        result.setDateFormat(new SimpleDateFormat(DateUtils.STANDARD_FORMAT));
        return result;
    }

}
