package com.apighost.orchestrator;

import com.apighost.model.GeneratedData;
import com.apighost.model.collector.FieldMeta;
import java.util.List;


/**
 * Interface for orchestrators that handle structured data generation based on provided metadata
 * fields.
 *
 * <p>Implementations of this interface are responsible for generating
 * structured data (e.g., API request bodies or parameters) by using metadata such as field names
 * and types, possibly via third-party services like OpenAI.</p>
 *
 * @author kobenlys
 * @version BETA-0.0.1
 */
public interface DataGenerationOrchestrator {

    /**
     * Executes the data generation process using the provided field metadata and authorization
     * key.
     *
     * @param fieldMetas a list of metadata describing fields for which to generate data
     * @param openAiKey  the API key for authenticating with the data generation service
     * @return a list of generated data, or an empty list if generation fails
     */
    List<GeneratedData> executeGenerate(List<FieldMeta> fieldMetas, String openAiKey);
}
