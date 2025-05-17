package com.apighost.orchestrator;


import com.apighost.model.GenerateBody;


/**
 * Defines the contract for orchestrators responsible for generating structured data
 * (e.g., API request bodies or JSON payloads) based on a provided JSON schema or metadata.
 *
 * <p>Typical implementations may interact with external AI-powered services such as OpenAI
 * to synthesize realistic example data using the given metadata structure, which includes
 * field names, types, or example placeholders.</p>
 *
 * @author kobenlys
 * @version BETA-0.0.1
 */
public interface DataGenerationOrchestrator {

    /**
     * Executes the data generation process using the provided field metadata and authorization
     * key.
     *
     * @param jsonBody     the input metadata or schema in JSON format that guides how the data should be generated;
     *                     this is typically a structure describing fields, types, or example placeholders
     * @param openAiKey    the API key for authenticating with the data generation service
     * @return a generated JSON structure wrapped in {@link GenerateBody}, or an empty structure if generation fails
     */
    GenerateBody executeGenerate(String jsonBody, String openAiKey);
}
