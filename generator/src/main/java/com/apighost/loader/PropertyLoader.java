package com.apighost.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads application properties from a predefined properties file using the Singleton pattern.
 * <p>
 * This class reads configuration values from {@code openai.properties} located in the classpath.
 * It provides a globally accessible instance via {@link #getInstance()}.
 * </p>
 *
 * @author kobenlys
 * @version BETA-0.0.1
 */
public class PropertyLoader {

    private final Properties properties = new Properties();

    /**
     * Loads the {@code openai.properties} file from the classpath during initialization.
     *
     * @throws IllegalArgumentException if the properties file is not found
     * @throws IllegalStateException if the properties file cannot be loaded
     */
    private PropertyLoader() {
        try (InputStream propertiesFile = getClass().getClassLoader()
            .getResourceAsStream("openai.properties")) {

            if (propertiesFile == null) {
                throw new IllegalArgumentException("Not Found .properties File");
            }
            properties.load(propertiesFile);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load .properties", e);
        }
    }

    /**
     * Holder class for lazy-loaded singleton instance.
     */
    private static class SingletonHolder {

        private static final PropertyLoader propertyLoader = new PropertyLoader();
    }

    /**
     * Returns the singleton instance of {@code PropertyLoader}.
     *
     * @return the {@code PropertyLoader} instance
     */
    public static PropertyLoader getInstance() {

        return SingletonHolder.propertyLoader;
    }

    /**
     * Retrieves the property value associated with the given key.
     *
     * @param key the property key
     * @return the property value, or {@code null} if the key does not exist
     */
    public String get(String key) {
        return properties.getProperty(key);
    }
}
