package com.apighost.parser.scenario.writer;

import com.apighost.model.scenario.result.ScenarioResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

/**
 * JSON implementation of {@link ScenarioResultWriter}. Serializes the scenario result into a
 * pretty-printed JSON file.
 * <p>
 * Example usage: new JsonScenarioResultWriter().writeScenarioResult(result, "output.json");
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class JsonScenarioResultWriter implements ScenarioResultWriter {

    private final ObjectMapper objectMapper;

    public JsonScenarioResultWriter() {
        this.objectMapper = new ObjectMapper();
        /** Format the JSON output in a human-readable (pretty-printed) way. */
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Writes the provided {@link ScenarioResult} object to a JSON file at the specified path. If
     * the file does not exist, it will be created. If it already exists, it will be overwritten.
     *
     * @param scenarioResult the scenario result object to serialize
     * @param filePath       the absolute or relative path to the output JSON file
     * @throws RuntimeException if an I/O or serialization error occurs during writing
     */
    @Override
    public void writeScenarioResult(ScenarioResult scenarioResult, String filePath) {
        File outputFile = new File(filePath);

        if (outputFile.isDirectory()) {
            throw new RuntimeException(
                "Cannot write to path: " + filePath + " (it is a directory)");
        }

        try {
            objectMapper.writeValue(outputFile, scenarioResult);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write ScenarioResult to file: " + filePath, e);
        }
    }
}
