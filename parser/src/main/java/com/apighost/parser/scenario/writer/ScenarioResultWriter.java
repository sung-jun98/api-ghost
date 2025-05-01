package com.apighost.parser.scenario.writer;

import com.apighost.model.scenario.result.ScenarioResult;

/**
 * Interface for writing {@link ScenarioResult} data to a file.
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public interface ScenarioResultWriter {

    /**
     * Writes the given {@link ScenarioResult} to the specified file path.
     *
     * @param scenarioResult the scenario result to be written
     * @param filePath       the absolute or relative path to the output file
     * @throws RuntimeException if writing fails due to I/O or serialization error
     */
    public void writeScenarioResult(ScenarioResult scenarioResult, String filePath);
}
