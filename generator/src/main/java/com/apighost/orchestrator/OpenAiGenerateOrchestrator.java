package com.apighost.orchestrator;

import com.apighost.generate.StructuredDataGenerator;
import com.apighost.model.GenerateBody;
import com.apighost.model.collector.FieldMeta;
import com.apighost.openai.FieldMetaPromptBuilder;
import com.apighost.openai.OpenAiStructuredDataGenerator;
import com.apighost.parser.OpenAiResponseParser;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Orchestrator that handles the process of generating structured data by communicating with the
 * OpenAI API.
 *
 * <p>This class builds a prompt using {@link FieldMeta} metadata, sends it to
 * OpenAI using the {@link StructuredDataGenerator}, and parses the response into
 * {@link GenerateBody}
 * </p>
 *
 * @author kobenlys
 * @version BETA-0.0.1
 */
public class OpenAiGenerateOrchestrator implements DataGenerationOrchestrator {

    private final StructuredDataGenerator dataGenerator = OpenAiStructuredDataGenerator.getInstance();

    public GenerateBody executeGenerate(String jsonBody, String openAiKey) {
        try {

            String prompt = FieldMetaPromptBuilder.buildPrompt(jsonBody);
            String response = dataGenerator.generate(prompt, openAiKey);
            return OpenAiResponseParser.extractResponse(response);
        } catch (JsonProcessingException e) {
            return new GenerateBody();
        }
    }
}
