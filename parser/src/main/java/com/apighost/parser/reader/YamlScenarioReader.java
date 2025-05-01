package com.apighost.parser.reader;

import com.apighost.model.scenario.testfile.Scenario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class YamlScenarioReader implements ScenarioReader {

    @Override
    public Scenario readScenario(String yamlFilePath) throws IOException {
        // YAML을 처리할 수 있는 ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

        // YAML 파일을 읽어서 Scenario DTO로 변환
        File yamlFile = new File(yamlFilePath);
        Scenario scenario = objectMapper.readValue(yamlFile, Scenario.class);

        return scenario;
    }

    @Override
    public void writeScenario(Scenario scenario) {

    }

}
