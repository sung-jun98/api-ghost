package com.apighost.openai;

import com.apighost.loader.PropertyLoader;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Utility class responsible for constructing a prompt string used for OpenAI-based
 * data generation, based on a given JSON structure.
 *
 * <p>This builder appends predefined start and end prompt templates (loaded from
 * application properties) around the provided JSON schema. The resulting prompt
 * is used to instruct the language model on how to generate realistic example data.</p>
 *
 * @author kobenlys
 * @version BETA-0.0.1
 */
public class FieldMetaPromptBuilder {

    private static final PropertyLoader propertyLoader = PropertyLoader.getInstance();

    /**
     * Builds a full prompt string by embedding the given JSON structure between
     * the start and end prompt templates.
     *
     * @param jsonBody the JSON string representing the field structure to be used as prompt content
     * @return a formatted prompt string ready to be sent to OpenAI or another LLM
     * @throws JsonProcessingException if the JSON content cannot be processed (reserved for future extension)
     */
    public static String buildPrompt(String jsonBody)
        throws JsonProcessingException {
        return propertyLoader.get("openai.prompt.start") + jsonBody;
    }
}