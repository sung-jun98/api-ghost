package com.apighost.openai;

import com.apighost.generate.StructuredDataGenerator;
import com.apighost.loader.PropertyLoader;
import com.apighost.model.ObjectMapperHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link StructuredDataGenerator} that interacts with OpenAI's API to generate
 * structured data based on a given prompt.
 *
 * <p>This class is implemented as a singleton and uses {@code gpt-3.5-turbo} model by default.
 * It relies on configuration values loaded via {@link PropertyLoader} and uses a shared
 * {@link ObjectMapper} instance from {@link ObjectMapperHolder}.</p>
 *
 * @author kobenlys
 * @version BETA-0.0.1
 */
public class OpenAiStructuredDataGenerator implements StructuredDataGenerator {

    private final ObjectMapper objectMapper;
    private final PropertyLoader propertyLoader;

    private static final Logger log = LoggerFactory.getLogger(OpenAiStructuredDataGenerator.class);

    public OpenAiStructuredDataGenerator() {
        objectMapper = ObjectMapperHolder.getInstance();
        propertyLoader = PropertyLoader.getInstance();
    }

    /**
     * Holder class for thread-safe lazy-loaded singleton instance.
     */
    private static class SingletonHolder {

        private static final OpenAiStructuredDataGenerator openAiStructuredDataGenerator = new OpenAiStructuredDataGenerator();
    }

    /**
     * Returns the singleton instance of {@code OpenAiStructuredDataGenerator}.
     *
     * @return singleton instance
     */
    public static OpenAiStructuredDataGenerator getInstance() {

        return SingletonHolder.openAiStructuredDataGenerator;
    }

    /**
     * Sends a prompt to the OpenAI API and returns the response as a string.
     *
     * @param prompt    the input prompt string to send
     * @param openAiKey the OpenAI API key for authentication
     * @return the API response body, or an empty string in case of error
     */
    @Override
    public String generateStructuredData(String prompt, String openAiKey) {
        try {

            Map<String, Object> request = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                    Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7
            );
            String requestBody = objectMapper.writeValueAsString(request);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(propertyLoader.get("openai.prompt.url")))
                .header("Authorization", "Bearer " + openAiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (InterruptedException e) {
            log.error("Failed to OpenAi Connections");
            return "";
        } catch (IOException e) {
            log.error("Failed to Parsing FieldMeta");
            return "";
        }
    }
}
