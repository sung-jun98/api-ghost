package parser.scenario.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apighost.model.scenario.step.HTTPMethod;
import com.apighost.model.scenario.ScenarioResult;
import com.apighost.model.scenario.result.ResultStep;
import com.apighost.parser.scenario.reader.JsonScenarioResultReader;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * Unit tests for {@link JsonScenarioResultReader}.
 * <p>
 * This test verifies that the reader can correctly deserialize a JSON file into a
 * {@link ScenarioResult} object, and that it handles invalid paths gracefully.
 * </p>
 *
 * @author haazz
 * @version BETA-0.0.1
 */
class JsonScenarioResultReaderTest {

    private File tempJsonFile;

    /**
     * Sets up a temporary JSON file with valid scenario content before each test.
     *
     * @throws IOException if writing the test file fails
     */
    @BeforeEach
    void setUp() throws IOException {
        String jsonContent = """
             {
               "name": "Signup Scenario",
               "description": "Validate new user registration and login process",
               "executedAt": "2025-04-23T14:15:00.000",
               "totalDurationMs": 550,
               "averageDurationMs": 275,
               "filePath": "/local/result/user",
               "baseUrl": "http://localhost:8080",
               "isScenarioSuccess": true,
               "results": [
                 {
                   "stepName": "signup",
                   "type": "HTTP",
                   "url": "http://localhost:8080/api/signup",
                   "method": "POST",
                   "requestBody": {
                     "json": "{ \\"email\\": \\"test@test.com\\", \\"password\\": \\"1234\\" }"
                   },
                   "requestHeader": {
                     "Authorization": "Bearer abc.def.ghi",
                     "Content-Type": "application/json"
                   },
                   "responseBody": "{ \\"status\\": \\"success\\" }",
                   "responseHeaders": {
                     "Content-Type": "application/json"
                   },
                   "status": 200,
                   "startTime": "2025-04-23T14:15:01.000",
                   "endTime": "2025-04-23T14:15:01.300",
                   "durationMs": 300,
                   "requestSuccess": true,
                   "route": [
                     {
                       "expected": {
                         "status": "200",
                         "value": {
                           "field": "value"
                         }
                       },
                       "then": {
                         "store": {
                           "postId": "123"
                         },
                         "step": "createPost"
                       }
                     }
                   ]
                 }
               ]
             }
            \s""";

        tempJsonFile = File.createTempFile("scenario-result", ".json");
        try (FileWriter writer = new FileWriter(tempJsonFile)) {
            writer.write(jsonContent);
        }
    }

    /**
     * Deletes the temporary test file after each test run.
     */
    @AfterEach
    void tearDown() {
        if (tempJsonFile != null && tempJsonFile.exists()) {
            tempJsonFile.delete();
        }
    }

    /**
     * Tests that {@link JsonScenarioResultReader#readScenarioResult(String)} correctly parses a
     * valid JSON file and returns a populated {@link ScenarioResult}.
     *
     * @throws IOException if file reading fails
     */
    @Test
    void readScenarioResult_shouldReturnValidScenarioResult() throws IOException {
        // given
        JsonScenarioResultReader reader = new JsonScenarioResultReader();

        // when
        ScenarioResult result = reader.readScenarioResult(tempJsonFile.getAbsolutePath());

        // then
        assertNotNull(result);
        assertEquals("Signup Scenario", result.getName());
        assertEquals("/local/result/user", result.getFilePath());
        assertTrue(result.isScenarioSuccess());
        assertEquals(1, result.getResults().size());

        ResultStep step = result.getResults().get(0);
        assertEquals("http://localhost:8080/api/signup", step.getUrl());
        assertEquals(HTTPMethod.POST, step.getMethod());
        assertEquals(200, step.getStatus());
    }

    /**
     * Tests that {@link JsonScenarioResultReader#readScenarioResult(String)} throws an
     * {@link IOException} when the given path is invalid or the file does not exist.
     */
    @Test
    void readScenarioResult_withInvalidPath_shouldThrowIOException() {
        // given
        JsonScenarioResultReader reader = new JsonScenarioResultReader();
        String invalidPath = "nonexistent/path/to/file.json";

        // then
        assertThrows(IOException.class, () -> reader.readScenarioResult(invalidPath));
    }
}
