package com.apighost.parser.scenario.reader;

import com.apighost.model.scenario.Scenario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class YamlScenarioReader implements ScenarioReader {

    /**
     * Convert the YAML scenario information stored in the local directory to DTO
     *
     * @param yamlFilePath The path of the YAML file to be read
     * @return Scenario Scenario information converted to DTO
     * @throws IOException
     */
    @Override
    public Scenario readScenario(String yamlFilePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

        File yamlFile = new File(yamlFilePath);
        try {
            Scenario scenario = objectMapper.readValue(yamlFile, Scenario.class);

            return scenario;
        } catch (IOException e) {
            return null;
        }
    }
}
