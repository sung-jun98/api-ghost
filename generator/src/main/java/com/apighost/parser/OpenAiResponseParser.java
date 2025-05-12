package com.apighost.parser;

import com.apighost.model.GeneratedData;
import com.apighost.model.ObjectMapperHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Parses the JSON response from OpenAI and extracts structured data into a list of
 * {@link GeneratedData} objects.
 *
 * <p>This class expects the JSON response to follow the OpenAI chat completion
 * format, with a "choices" array containing a "message" object and its "content" string.</p>
 *
 * @author kobenlys
 * @version BETA-0.0.1
 */
public class OpenAiResponseParser {

    private static final ObjectMapper objectMapper = ObjectMapperHolder.getInstance();

    /**
     * Extracts the list of generated data from the OpenAI API JSON response.
     *
     * @param jsonResponse the raw JSON string returned from the OpenAI API
     * @return a list of {@link GeneratedData} representing the key-value data structure
     * @throws JsonProcessingException  if the input JSON cannot be parsed
     * @throws IllegalArgumentException if the JSON structure does not contain expected fields
     */
    public static List<GeneratedData> extractResponse(String jsonResponse)
        throws JsonProcessingException {

        if (jsonResponse == null || jsonResponse.isEmpty()) {
            return List.of();
        }

        JsonNode rootResponse = objectMapper.readTree(jsonResponse);

        if (rootResponse.path("choices").isEmpty()) {
            throw new IllegalArgumentException(
                "Unexpected response format: 'choices' array is empty.");
        }

        String generatedBodyStr = rootResponse.path("choices").get(0).path("message")
            .path("content").asText();

        Map<String, Object> generatedBody = objectMapper.readValue(generatedBodyStr,
            new TypeReference<Map<String, Object>>() {
            });

        return generatedBody.entrySet().stream()
            .map(entry -> new GeneratedData(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }
}
