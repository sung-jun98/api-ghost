package com.apighost.parser.scenario.writer;

import com.apighost.model.scenario.Scenario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;


/**
 * JSON implementation of {@link YamlScenarioWriter}. Serializes the scenario into a
 * pretty-printed YAML file.
 * <p>
 * Example usage: new JsonScenarioResultWriter().writeScenario(result, "output.json");
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class YamlScenarioWriter implements ScenarioWriter {

    private final ObjectMapper objectMapper;

    public YamlScenarioWriter() {
        this.objectMapper = new ObjectMapper(new YAMLFactory());
        /** Format the output in a human-readable (pretty-printed) way. */
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * A method that converts Scenario DTO to YAML file
     *
     * @param scenario Scenario dto
     * @throws IOException I/O exceptions that occur when file I/O
     */
    @Override
    public void writeScenario(Scenario scenario, String filePath) throws IOException {
        File yamlFile = new File(filePath);

        try {
            objectMapper.writeValue(yamlFile, scenario);
        } catch (IOException e) {
            throw new IOException("Error occurs while writing YAML files", e);
        }

    }
}
