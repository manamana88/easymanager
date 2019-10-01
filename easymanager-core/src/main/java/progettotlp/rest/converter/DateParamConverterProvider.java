package progettotlp.rest.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import progettotlp.facilities.DateUtils;

@Provider
public class DateParamConverterProvider implements ParamConverterProvider {

    private static Logger logger = LoggerFactory.getLogger(DateParamConverterProvider.class);
    
	@Override
	public <T> ParamConverter<T> getConverter(Class<T> rawType, Type arg1, Annotation[] arg2) {
		if (Date.class != rawType){
			return null;
		}
		
		return new ParamConverter<T> (){

			@Override
			@SuppressWarnings("unchecked")
			public T fromString(String arg0) {
				try {
					return (T) DateUtils.parseDate(arg0);
				} catch (ParseException e) {
					logger.error("Unable to parse date: ", e);
				}
				return null;
			}

			@Override
			public String toString(T arg0) {
				return DateUtils.formatDate((Date) arg0);
			}

		};
	}

}
