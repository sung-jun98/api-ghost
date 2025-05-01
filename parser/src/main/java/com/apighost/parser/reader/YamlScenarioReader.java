package com.apighost.parser.reader;

import com.apighost.model.scenario.testfile.Scenario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class YamlScenarioReader implements ScenarioReader {

    private final ObjectMapper objectMapper;

    /**
     * Objectmapper initialization for yaml parsing
     */
    private YamlScenarioReader() {
        this.objectMapper = new ObjectMapper(new YAMLFactory());
    }

    @Override
    public Scenario readScenario() {
        parseFromFile(" ");
        return null;
    }

    /**
     * Fassing the scenario YAML from the file path
     *
     * @param filePath yaml file path
     * @Return parsed scenariodto object
     * @throws IOException File Reading or parsing failure
     */
    public Scenario parseFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        return objectMapper.readValue(file, Scenario.class);
    }
}
