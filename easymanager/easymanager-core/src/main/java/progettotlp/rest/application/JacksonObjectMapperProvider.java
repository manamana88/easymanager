package progettotlp.rest.application;

import java.text.SimpleDateFormat;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import progettotlp.classes.AccountEmail;
import progettotlp.classes.Azienda;
import progettotlp.classes.Bene;
import progettotlp.classes.DdT;
import progettotlp.classes.Fattura;
import progettotlp.facilities.DateUtils;
import progettotlp.interfaces.AccountEmailInterface;
import progettotlp.interfaces.AziendaInterface;
import progettotlp.interfaces.BeneInterface;
import progettotlp.interfaces.DdTInterface;
import progettotlp.interfaces.FatturaInterface;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
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
        result.setSerializationInclusion(Include.ALWAYS);
        result.setDateFormat(new SimpleDateFormat(DateUtils.STANDARD_FORMAT));
        Module interfaceDeserializationModule = createInterfaceDeserializationModule();
		result.registerModule(interfaceDeserializationModule);
        return result;
    }

	private static Module createInterfaceDeserializationModule() {
		SimpleModule module = new SimpleModule("InterfaceDeserializationModule", Version.unknownVersion());

		SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
		resolver.addMapping(FatturaInterface.class, Fattura.class);
		resolver.addMapping(DdTInterface.class, DdT.class);
		resolver.addMapping(BeneInterface.class, Bene.class);
		resolver.addMapping(AziendaInterface.class, Azienda.class);
		resolver.addMapping(AccountEmailInterface.class, AccountEmail.class);

		module.setAbstractTypes(resolver);
		return module;
	}

}
