package com.apighost.parser.scenario.writer;

import com.apighost.model.scenario.Scenario;
import java.io.IOException;

/**
 * Interface for writing {@link Scenario} data to a file.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public interface ScenarioWriter {
    /**
     * Writes the given {@link Scenario} to the specified file path.
     *
     * @param scenario the scenario result to be written
     * @param filePath       the absolute or relative path to the output file
     * @throws IOException if writing fails due to I/O or serialization error
     */
    public void writeScenario(Scenario scenario, String filePath) throws IOException;
}
