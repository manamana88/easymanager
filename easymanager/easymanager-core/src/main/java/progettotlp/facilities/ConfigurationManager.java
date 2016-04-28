/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.facilities;

import java.io.File;
import java.io.FileReader;
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
        	if (property==null || property.trim().isEmpty()){
        		property = "easymanager.properties";
        	}
            File propertiesFile = new File(property);
            if (!propertiesFile.exists()) {
                throw new IllegalArgumentException(propertiesFile.getAbsolutePath() + " file not found");
            } else {
                if (properties == null){
                    properties = new Properties();
                }
                properties.load(new FileReader(propertiesFile));
            }
        } catch (Exception ex) {
            logger.error("Unable to initialize ConfigurationManager: ", ex);
            throw new IllegalArgumentException("Unable to initialize ConfigurationManager: ", ex);
        }
    }
    
    public static Float getIvaDefault(){
        return Float.parseFloat(properties.getProperty(Property.IVA_DEFAULT.value));
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
    	FATTURE_FOLDER_PATH("fatture_folder"),
    	DDT_FOLDER_PATH("ddt_folder"),
    	EXTERNAL_RESOURCES("external_resources");
    	
    	private String value;

		private Property(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
    }
}
