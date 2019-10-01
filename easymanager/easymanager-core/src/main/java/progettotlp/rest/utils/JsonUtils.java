package progettotlp.rest.utils;

import java.text.ParseException;
import java.util.Date;

import progettotlp.facilities.DateUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonUtils {
	public static Date getDateValue(ObjectNode ddt, String fieldName) throws ParseException {
		String textValue = getTextValue(ddt, fieldName);
		return textValue != null ? DateUtils.parseDate(textValue) : null; 
	}
	
	public static Float getFloatValue(ObjectNode ddt, String fieldName) {
		Number numberValue = getNumberValue(ddt, fieldName);
		return numberValue != null ? numberValue.floatValue() : null;
	}
	
	public static Double getDoubleValue(ObjectNode ddt, String fieldName) {
		Number numberValue = getNumberValue(ddt, fieldName);
		return numberValue != null ? numberValue.doubleValue() : null;
	}
	
	public static Long getLongValue(ObjectNode ddt, String fieldName) {
		Number numberValue = getNumberValue(ddt, fieldName);
		return numberValue != null ? numberValue.longValue() : null;
	}

	public static Integer getIntValue(ObjectNode ddt, String fieldName) {
		Number numberValue = getNumberValue(ddt, fieldName);
		return numberValue != null ? numberValue.intValue() : null;
	}

	public static Boolean getBooleanValue(ObjectNode ddt, String fieldName) {
		JsonNode jsonNode = ddt.get(fieldName);
		return jsonNode != null ? jsonNode.booleanValue() : null;
	}
	
	public static Number getNumberValue(ObjectNode ddt, String fieldName) {
		JsonNode jsonNode = ddt.get(fieldName);
		if (jsonNode != null){
			return jsonNode.numberValue();
		}
		return null;
	}
	
	public static String getTextValue(ObjectNode ddt, String fieldName) {
		JsonNode jsonNode = ddt.get(fieldName);
		if (jsonNode != null){
			return jsonNode.textValue();
		} else {
			return "";
		}
	}
}
