package com.apighost.parser.scenario.writer;

import com.apighost.model.scenario.test.Scenario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;

/**
 * Scenario Dto -> YAML
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
     * Scenario Dto -> YAML
     *
     * @param scenario Scenario dto
     * @throws IOException I/O exceptions that occur when file I/O
     */
    @Override
    public void writeScenario(Scenario scenario) throws IOException {
        /** The creation path must be modified later in accordance with the actual distribution environment.*/
        String yamlFilePath = "src/test/resources/parser/writeScenarioExample.yaml";
        File yamlFile = new File(yamlFilePath);

        try {
            objectMapper.writeValue(yamlFile, scenario);
        } catch (IOException e) {
            throw new IOException("Error occurs while writing YAML files", e);
        }

    }
}
