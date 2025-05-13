package com.apighost.parser.scenario.reader;

import com.apighost.model.scenario.ScenarioResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 * JSON implementation of {@link ScenarioResultReader}. Serializes the scenario result into a
 *
 * <p>Example usage:</p>
 * <pre>
 *     new JsonScenarioResultWriter().writeScenarioResult(result, "output.json");
 * </pre>
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class JsonScenarioResultReader implements ScenarioResultReader {

    ObjectMapper objectMapper;

    public JsonScenarioResultReader() {
        objectMapper = new ObjectMapper();
    }

    /**
     * Reads and deserializes a {@link ScenarioResult} from the JSON file path.
     *
     * @param filePath the path to the JSON file containing scenario result
     * @return the deserialized {@link ScenarioResult} object
     * @throws IOException if the file is not found, invalid, or cannot be parsed
     */
    @Override
    public ScenarioResult readScenarioResult(String filePath) throws IOException {
        File inputFile = new File(filePath);
        if (!inputFile.isFile()) {
            throw new IOException("Invalid file path: " + filePath);
        }
        return objectMapper.readValue(inputFile, ScenarioResult.class);
    }
}
