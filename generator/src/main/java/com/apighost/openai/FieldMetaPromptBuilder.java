package com.apighost.openai;

import com.apighost.loader.PropertyLoader;
import com.apighost.model.ObjectMapperHolder;
import com.apighost.model.collector.FieldMeta;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

/**
 * Utility class for building a prompt string using a list of {@link FieldMeta} objects.
 * <p>
 * The prompt format is defined by the properties {@code openai.prompt.start} and
 * {@code openai.prompt.end}, which are loaded from the configuration file via
 * {@link PropertyLoader}.
 * </p>
 *
 * @author kobenlys
 * @version BETA-0.0.1
 */
public class FieldMetaPromptBuilder {

    private static final ObjectMapper objectMapper = ObjectMapperHolder.getInstance();
    private static final PropertyLoader propertyLoader = PropertyLoader.getInstance();

    /**
     * Builds a formatted prompt string from the given list of {@link FieldMeta} objects. The
     * resulting string includes a prefix and suffix defined in the properties file.
     *
     * @param fieldMetas the list of field metadata to include in the prompt
     * @return the constructed prompt string
     * @throws JsonProcessingException if an error occurs while serializing {@code fieldMetas}
     */
    public static String buildPrompt(List<FieldMeta> fieldMetas)
        throws JsonProcessingException {

        return propertyLoader.get("openai.prompt.start") + objectMapper.writeValueAsString(
            fieldMetas) + "\n" + propertyLoader.get("openai.prompt.end");
    }
}
