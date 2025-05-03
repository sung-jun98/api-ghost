package com.apighost.parser.scenario.reader;

import com.apighost.model.scenario.Scenario;
import com.apighost.model.scenario.ScenarioResult;
import java.io.IOException;

/**
 * Interface for reading {@link Scenario} objects from external file.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public interface ScenarioReader {
    /**
     * Reads a Scenario from the given file path.
     *
     * @param filePath path to the result file
     * @return deserialized Scenario object
     * @throws IOException if reading or parsing fails
     */
    public Scenario readScenario (String filePath) throws IOException;

}
