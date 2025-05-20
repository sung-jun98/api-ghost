package com.apighost.web.controller;

import com.apighost.model.GenerateBody;
import com.apighost.orchestrator.DataGenerationOrchestrator;
import com.apighost.orchestrator.OpenAiGenerateOrchestrator;
import com.apighost.web.util.JsonUtils;
import com.apighost.web.util.OpenAIKeyHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.Map;

/**
 * On the GUI, the controller using the prompt of Openai to use when creating parameter data in
 * randomly
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class GenerateDataController implements ApiController {

    private final DataGenerationOrchestrator orchestrator = new OpenAiGenerateOrchestrator();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static OpenAIKeyHolder openAIKeyHolder;

    /**
     * 1. Read the file from `user/.apigost/api-kost.yaml in your computer.
     * 2. Passing the read YAML file as Java Model
     * 3. Receive the user's Openaikey from parsing results,
     * 4. Use the read Openaikey to bring the initialized data values from the prompt.
     *
     * @param request  JSON value before format transfer
     * @param response Created through the prompt of Openai JsonData
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        StringBuilder requestBody = new StringBuilder();
        openAIKeyHolder = new OpenAIKeyHolder();

        String apiKey = openAIKeyHolder.getOpenAIKey();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }

        Map<String, String> requestMap = objectMapper.readValue(requestBody.toString(), Map.class);

        String jsonBody = requestMap.get("jsonBody");
        GenerateBody generateBody = orchestrator.executeGenerate(jsonBody, apiKey);

        JsonUtils.writeJsonResponse(response, generateBody, 200);
    }
}
