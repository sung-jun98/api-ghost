package com.apighost.parser.loadtest.converter;

import com.apighost.model.loadtest.parameter.LoadTestExecuteParameter;
import com.apighost.model.loadtest.parameter.LoadTestExecuteParameter.Builder;
import com.apighost.model.loadtest.parameter.LoadTestParameter;
import com.apighost.model.scenario.Scenario;
import com.apighost.parser.scenario.reader.ScenarioReader;

import com.apighost.util.file.BasePathHolder;
import com.apighost.util.file.FileType;
import com.apighost.util.file.FileUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Converter converts LoadtestParameter objects to LoadtestExecuteparameter objects.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class LoadTestParameterConverter {

    private final ScenarioReader scenarioReader;

    public LoadTestParameterConverter(ScenarioReader scenarioReader) {
        this.scenarioReader = scenarioReader;
    }

    /**
     * Based on the name scenario inside the LoadtestParameter, the YAML files inside the computer
     * are parsed and into a list of actual scenario objects. Convert it.
     *
     * @param parameter
     * @return LoadTestExecuteParameter
     * @throws IOException
     */
    public LoadTestExecuteParameter convert(LoadTestParameter parameter) throws IOException {
        List<Scenario> scenarios = new ArrayList<>();

        for (String scenarioName : parameter.getScenarios()) {

            Path configDirectory = FileUtil.findDirectory(FileType.SCENARIO,
                BasePathHolder.getInstance()
                    .getBasePath());
            Path yamlConfigFilePath = configDirectory.resolve(scenarioName);
            if (!Files.exists(yamlConfigFilePath)) {
                throw new IOException("Invalid scenario name: " + scenarioName);
            }

            scenarios.add(scenarioReader.readScenario(yamlConfigFilePath.toString()));
        }

        return new Builder()
            .name(parameter.getName())
            .description(parameter.getDescription())
            .loadTest(parameter.getLoadTest())
            .scenarios(scenarios)
            .build();
    }
}
