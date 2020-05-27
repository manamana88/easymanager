package progettotlp.facilities;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CloudNativeUtilsTest {

    public static final int RANDOM_STRING_LENGTH = 32;
    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Test
    public void testGetIntegerNoEnv() {
        String envProperty = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
        assertNull(CloudNativeUtils.getInteger(envProperty));
    }

    @Test
    public void testGetIntegerNotParsable() {
        String envProperty = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
        environmentVariables.set(envProperty, "NOT_PARSABLE");
        assertNull(CloudNativeUtils.getInteger(envProperty));
    }

    @Test
    public void testGetInteger() {
        String envProperty = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
        environmentVariables.set(envProperty, "25");
        Integer expected = 25;
        assertEquals(expected, CloudNativeUtils.getInteger(envProperty));
    }

    @Test
    public void testGetIntegerWithDefaultValueNoEnv() {
        String envProperty = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
        assertNull(CloudNativeUtils.getInteger(envProperty, null));
        Integer expected = 1;
        assertEquals(expected, CloudNativeUtils.getInteger(envProperty, 1));
    }

    @Test
    public void testGetIntegerWithDefaultValueEnvNotParsable() {
        String envProperty = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
        environmentVariables.set(envProperty, "NOT_PARSABLE_INTEGER");
        assertNull(CloudNativeUtils.getInteger(envProperty, null));
        Integer expected = 1;
        assertEquals(expected, CloudNativeUtils.getInteger(envProperty, 1));
    }

    @Test
    public void testGetIntegerWithDefaultValue() {
        String envProperty = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
        environmentVariables.set(envProperty, "6");
        Integer expectedValue = 6;
        assertEquals(expectedValue, CloudNativeUtils.getInteger(envProperty, null));
        assertEquals(expectedValue, CloudNativeUtils.getInteger(envProperty, 1));
    }

    @Test
    public void testGetLongNoEnv() {
        String envProperty = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
        assertNull(CloudNativeUtils.getLong(envProperty));
    }

    @Test
    public void testGetLongNotParsable() {
        String envProperty = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
        environmentVariables.set(envProperty, "NOT_PARSABLE");
        assertNull(CloudNativeUtils.getLong(envProperty));
    }

    @Test
    public void testGetLong() {
        String envProperty = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
        environmentVariables.set(envProperty, "25");
        Long expected = 25L;
        assertEquals(expected, CloudNativeUtils.getLong(envProperty));
    }

    @Test
    public void testGetLongWithDefaultValueNoEnv() {
        String envProperty = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
        assertNull(CloudNativeUtils.getLong(envProperty, null));
        Long expectedResult = 1L;
        assertEquals(expectedResult, CloudNativeUtils.getLong(envProperty, 1L));
    }

    @Test
    public void testGetLongWithDefaultValueEnvNotParsable() {
        String envProperty = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
        environmentVariables.set(envProperty, "NOT_PARSABLE_INTEGER");
        assertNull(CloudNativeUtils.getLong(envProperty, null));
        Long expectedResult = 1L;
        assertEquals(expectedResult, CloudNativeUtils.getLong(envProperty, 1L));
    }

    @Test
    public void testGetLongWithDefaultValue() {
        String envProperty = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
        environmentVariables.set(envProperty, "6");
        Long expectedValue = 6L;
        assertEquals(expectedValue, CloudNativeUtils.getLong(envProperty, null));
        assertEquals(expectedValue, CloudNativeUtils.getLong(envProperty, 1L));
    }

    @Test
    public void testGetEnvOrPropertyNoProperty() {
        String propertyName = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
        assertNull(CloudNativeUtils.getEnvOrProperty(propertyName));
    }

    @Test
    public void testGetEnvOrPropertyOnlyProperty() {
        String propertyName = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
        String propertyValue = "myValue1";
        System.setProperty(propertyName, propertyValue);
        assertEquals(propertyValue, CloudNativeUtils.getEnvOrProperty(propertyName));
    }

    @Test
    public void testGetEnvOrPropertyOnlyEnv() {
        String propertyName = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
        String propertyValue = "myValue2";
        environmentVariables.set(propertyName, propertyValue);
        assertEquals(propertyValue, CloudNativeUtils.getEnvOrProperty(propertyName));
    }

    @Test
    public void testGetEnvOrPropertyBothEnvAndPro() {
        String propertyName = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
        String envValue = "myValue3";
        String propValue = "myValue4";
        environmentVariables.set(propertyName, envValue);
        System.setProperty(propertyName, propValue);

        assertEquals(envValue, CloudNativeUtils.getEnvOrProperty(propertyName));
    }

    @Test
    public void testGetEnvOrPropertyWithDefaultNoProperty() {
        String propertyName = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
        String defaultValue = "defaultValue1";
        assertEquals(defaultValue, CloudNativeUtils.getEnvOrProperty(propertyName, defaultValue));
    }

    @Test
    public void testGetEnvOrPropertyWithDefaultOnlyProperty() {
        String propertyName = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
        String propertyValue = "myValue1";
        System.setProperty(propertyName, propertyValue);
        assertEquals(propertyValue, CloudNativeUtils.getEnvOrProperty(propertyName, "defaultValue2"));
    }

    @Test
    public void testGetEnvOrPropertyWithDefaultOnlyEnv() {
        String propertyName = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
        String propertyValue = "myValue2";
        environmentVariables.set(propertyName, propertyValue);
        assertEquals(propertyValue, CloudNativeUtils.getEnvOrProperty(propertyName, "defaultValue3"));
    }

    @Test
    public void testGetEnvOrPropertyWithDefaultBothEnvAndPro() {
        String propertyName = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
        String envValue = "myValue3";
        String propValue = "myValue4";
        environmentVariables.set(propertyName, envValue);
        System.setProperty(propertyName, propValue);

        assertEquals(envValue, CloudNativeUtils.getEnvOrProperty(propertyName, "defaultValue4"));
    }
}