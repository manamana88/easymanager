package progettotlp.facilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloudNativeUtils {

    private static final Logger LOG = LoggerFactory.getLogger(CloudNativeUtils.class);

    private CloudNativeUtils(){}

    /**
     * <p>This method emulates {@link Integer#getInteger(String)} but it checks in both env and props
     *
     * <p>If there is no property with the specified name, if the
     * specified name is empty or {@code null}, or if the property
     * does not have the correct numeric format, then {@code null} is
     * returned.
     *
     * @param envProperty The name of the property
     * @return the {@code Integer} value of the property.
     * @throws  SecurityException for the same reasons as
     *          {@link System#getProperty(String) System.getProperty}
     * @see     System#getProperty(String)
     * @see     System#getProperty(String, String)
     */
    public static Integer getInteger(String envProperty){
        return getInteger(envProperty, null);
    }

    /**
     * <p>This method emulates {@link Integer#getInteger(String, Integer)} but it checks in both env and props
     *
     * <p>The default value is
     * returned if there is no property of the specified name, if the
     * property does not have the correct numeric format, or if the
     * specified name is empty or {@code null}.
     *
     * @param envProperty The name of the property
     * @param defaultValue The default value of the property
     * @return the {@code Integer} value of the property.
     * @throws  SecurityException for the same reasons as
     *          {@link System#getProperty(String) System.getProperty}
     * @see     System#getProperty(String)
     * @see     System#getProperty(String, String)
     */
    public static Integer getInteger(String envProperty, Integer defaultValue){
        String propertyStringValue = getEnvOrProperty(envProperty);
        try {
            if (propertyStringValue!=null){
                return Integer.decode(propertyStringValue);
            }
        } catch (NumberFormatException e){
            String message = String.format("Cannot parse property with name [%s] and value [%s]"
                    , envProperty
                    , propertyStringValue);
            LOG.warn(message, e);
        }
        return defaultValue;
    }

    /**
     * <p>This method emulates {@link Long#getLong(String)} but it checks in both env and props
     *
     * <p>If there is no property with the specified name, if the
     * specified name is empty or {@code null}, or if the property
     * does not have the correct numeric format, then {@code null} is
     * returned.
     *
     * @param envProperty The name of the property
     * @return the {@code Long} value of the property.
     * @throws  SecurityException for the same reasons as
     *          {@link System#getProperty(String) System.getProperty}
     * @see     System#getProperty(String)
     * @see     System#getProperty(String, String)
     */
    public static Long getLong(String envProperty){
        return getLong(envProperty, null);
    }

    /**
     * <p>This method emulates {@link Long#getLong(String, Long)} but it checks in both env and props
     *
     * <p>The default value is
     * returned if there is no property of the specified name, if the
     * property does not have the correct numeric format, or if the
     * specified name is empty or {@code null}.
     *
     * @param envProperty The name of the property
     * @param defaultValue The default value of the property
     * @return the {@code Long} value of the property.
     * @throws  SecurityException for the same reasons as
     *          {@link System#getProperty(String) System.getProperty}
     * @see     System#getProperty(String)
     * @see     System#getProperty(String, String)
     */
    public static Long getLong(String envProperty, Long defaultValue){
        String propertyStringValue = getEnvOrProperty(envProperty);
        try {
            if (propertyStringValue!=null){
                return Long.decode(propertyStringValue);
            }
        } catch (NumberFormatException e){
            String message = String.format("Cannot parse property with name [%s] and value [%s]"
                    , envProperty
                    , propertyStringValue);
            LOG.warn(message, e);
        }
        return defaultValue;
    }

    public static String getEnvOrProperty(String envProperty) {
        return getEnvOrProperty(envProperty, null);
    }

    public static String getEnvOrProperty(String envProperty, String defaultValue) {
        return getEnvOrProperty(envProperty, defaultValue, false);
    }

    public static String getEnvOrProperty(String envProperty, String defaultValue, boolean allowEmpty) {
        String propertyStringValue = System.getenv(envProperty);
        if (propertyStringValue == null || (propertyStringValue.trim().isEmpty() && !allowEmpty)){
            propertyStringValue = System.getProperty(envProperty);
        }
        return propertyStringValue == null || (propertyStringValue.trim().isEmpty() && !allowEmpty) ? defaultValue : propertyStringValue;
    }
}
