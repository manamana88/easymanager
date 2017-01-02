package progettotlp.rest.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import progettotlp.facilities.DateUtils;

public class DateDeserializer extends JsonDeserializer<Date> {

	private static Logger logger = LoggerFactory.getLogger(DateDeserializer.class);
	
	@Override
	public Date deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		SimpleDateFormat format = new SimpleDateFormat(DateUtils.STANDARD_FORMAT);
        String date = p.getText();
        try {
			return format.parse(date);
		} catch (ParseException e) {
			logger.error("Cannot parse this date: ["+date+"]", e);
		}
        return null;
	}

}
