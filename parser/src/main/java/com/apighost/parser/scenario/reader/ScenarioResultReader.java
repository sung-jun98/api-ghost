package com.apighost.parser.scenario.reader;

import com.apighost.model.scenario.ScenarioResult;
import java.io.IOException;

/**
 * Interface for reading {@link ScenarioResult} objects from external file.
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public interface ScenarioResultReader {

    /**
     * Reads a ScenarioResult from the given file path.
     *
     * @param filePath path to the result file
     * @return deserialized ScenarioResult object
     * @throws IOException if reading or parsing fails
     */
    ScenarioResult readScenarioResult(String filePath) throws IOException;
}
