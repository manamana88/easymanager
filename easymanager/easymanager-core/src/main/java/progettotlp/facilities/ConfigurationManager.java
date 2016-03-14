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
    public static final String DEFAULT_IVA_PERC="iva_default";

    private static Logger logger = LoggerFactory.getLogger(ConfigurationManager.class);
    private static Properties properties;

    public static void init() {
        try {
            File propertiesFile = new File("easymanager.properties");
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

    public static String getProperty(String propertyName){
        return properties.getProperty(propertyName);
    }

    public static Float getIvaDefault(){
        return Float.parseFloat(properties.getProperty(DEFAULT_IVA_PERC));
    }

    public static void setProperties(Properties properties){
        ConfigurationManager.properties = properties;
    }
}
