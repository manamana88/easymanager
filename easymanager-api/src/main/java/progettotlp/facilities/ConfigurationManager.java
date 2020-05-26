/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.facilities;

import java.io.*;
import java.math.BigDecimal;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author vincenzo.barrea
 */
public class ConfigurationManager {
    public static final String PROPERTIES_PATH = "propertiesPath";

    private static Logger logger = LoggerFactory.getLogger(ConfigurationManager.class);
    private static Properties properties;

    public static void init() {
        try {
        	String property = System.getProperty(PROPERTIES_PATH);
            InputStream propertiesStream;
            if (property!=null && !property.trim().isEmpty()){
                File propertiesFile = new File(property);
                if (!propertiesFile.exists()) {
                    throw new IllegalArgumentException(propertiesFile.getAbsolutePath() + " file not found");
                }
                propertiesStream = new FileInputStream(propertiesFile);
            }  else {
                InputStream resource = ConfigurationManager.class.getClassLoader().getResourceAsStream("easymanager.properties");
                if (resource!=null){
                    propertiesStream = resource;
                } else {
                    String exception = String.format("Properties file not found or environment variable [%s] not set"
                            , PROPERTIES_PATH);
                    throw new IllegalArgumentException(exception);
                }
            }
            if (properties == null){
                properties = new Properties();
            }
            properties.load(propertiesStream);
        } catch (Exception ex) {
            logger.error("Unable to initialize ConfigurationManager: ", ex);
            throw new RuntimeException("Unable to initialize ConfigurationManager: ", ex);
        }
    }
    
    public static BigDecimal getIvaDefault(){
        return new BigDecimal(properties.getProperty(Property.IVA_DEFAULT.value));
    }
    
    public static BigDecimal getBolloLimit(){
    	return new BigDecimal(properties.getProperty(Property.BOLLO_LIMIT.value));
    }

    public static String getProperty(String propertyName){
        return properties.getProperty(propertyName);
    }
    
    public static String getProperty(Property property){
    	return properties.getProperty(property.value);
    }

    public static void setProperties(Properties properties){
        ConfigurationManager.properties = properties;
    }
    
    public enum Property {
    	IVA_DEFAULT("iva_default"), 
    	BOLLO_LIMIT("bollo_limit");

    	private String value;

		Property(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
    }
}
