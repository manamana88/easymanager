package progettotlp.rest.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import progettotlp.classes.Fattura;

public class FatturaSerializer extends JsonSerializer<Fattura> {

	@Override
	public void serialize(Fattura value, JsonGenerator gen,
			SerializerProvider serializers) throws IOException,
			JsonProcessingException {
		if (value!=null){
			gen.writeNumber(value.getId());
		}
	}

}
