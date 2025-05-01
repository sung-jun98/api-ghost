package com.apighost.parser.reader;

import com.apighost.model.scenario.testfile.Scenario;
import java.io.IOException;

public interface ScenarioReader {
    public Scenario readScenario (String yamlFilePath) throws IOException;
    public void writeScenario (Scenario scenario);

}
