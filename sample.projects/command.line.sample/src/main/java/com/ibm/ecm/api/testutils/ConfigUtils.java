package com.ibm.ecm.api.testutils;


import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class ConfigUtils {

    public static final String CONF_PROP_NAME = "config";
    public static final String DEFAULT_TEST_PROPERTIES = "/sample.properties";

    protected static Map<String, String> properties = null;

    /**
     * loads configuration from classpath. Loads the resource specified by the
     * config system property, else loads DEFAULT_TEST_PROPERTIES. Also saves them in
     * a static field in this class.
     */
    public static synchronized final Map<String, String> loadTestProperties() {
        String configResource = "";
        if (properties == null) {
            Properties testConfig = new Properties();

            configResource = System.getProperty(CONF_PROP_NAME);
            System.out.println("Reading system property " + CONF_PROP_NAME + " = " + configResource);

            if (configResource == null) {
                configResource = DEFAULT_TEST_PROPERTIES;
            }
            System.out.println("Reading test configuration file " + configResource);

            InputStream inStream = ConfigUtils.class.getResourceAsStream(configResource);
            if (inStream == null) {
                System.out.println("Test properties file '" + configResource + "' was not found in classpath.");
            } else {
                try {
                    testConfig.load(inStream);
                } catch (IOException e) {
                    System.out.println("Exception loading test properties file " + configResource + " msg:" +  e.getMessage());
                }
            }
            Map<String, String> map = new HashMap<String, String>();
            for (Entry<?, ?> entry : testConfig.entrySet()) {
                map.put((String) entry.getKey(), ((String) entry.getValue()).trim());
                System.out.println("loaded property" + entry.getKey() + " val=" + ((String) entry.getValue()).trim());
            }
            properties = map;
        }
        return properties;
    }

    /**
     * Returns the value with the specified key in the test properties file, or null if
     * no such property exists.
     */
    public static synchronized String getProperty(String key) {
        if (properties == null) {
            loadTestProperties();
        }

        return properties.get(key);
    }
}
