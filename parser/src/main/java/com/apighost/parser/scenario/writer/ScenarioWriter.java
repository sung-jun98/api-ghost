package com.apighost.parser.scenario.writer;

import com.apighost.model.scenario.Scenario;
import java.io.IOException;

public interface ScenarioWriter {

    public void writeScenario(Scenario scenario) throws IOException;
}
