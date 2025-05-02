package com.apighost.parser.scenario.reader;

import com.apighost.model.scenario.Scenario;
import java.io.IOException;

public interface ScenarioReader {
    public Scenario readScenario (String yamlFilePath) throws IOException;

}
