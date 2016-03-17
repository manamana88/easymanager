package progettotlp.rest.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import progettotlp.facilities.DateUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateSerializer extends JsonSerializer<Date> {

	@Override
	public void serialize(Date value, JsonGenerator gen,
			SerializerProvider serializers) throws IOException,
			JsonProcessingException {
		gen.writeString(new SimpleDateFormat(DateUtils.STANDARD_FORMAT).format(value));
	}

}
