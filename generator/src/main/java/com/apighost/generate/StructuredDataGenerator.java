package com.apighost.generate;

/**
 * Interface for generating structured data (e.g., JSON) from a given prompt using a language
 * model.
 * <p>
 * Implementations of this interface should handle communication with external services (such as
 * OpenAI) to generate data based on the provided prompt.
 * </p>
 *
 * @author kobenlys
 * @version BETA-0.0.1
 */
public interface StructuredDataGenerator {

    /**
     * Generates structured data (e.g., JSON) from the given prompt using a language model.
     *
     * @param prompt the prompt describing the structure or content to generate
     * @param privateKey the API key used to authenticate with the external service
     * @return the generated structured data as a string
     */
    String generateStructuredData(String prompt, String privateKey);
}