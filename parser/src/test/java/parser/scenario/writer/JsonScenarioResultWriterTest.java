package parser.scenario.writer;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apighost.model.scenario.request.RequestBody;
import com.apighost.model.scenario.step.Expected;
import com.apighost.model.scenario.step.HTTPMethod;
import com.apighost.model.scenario.step.ProtocolType;
import com.apighost.model.scenario.ScenarioResult;
import com.apighost.model.scenario.result.ResultStep;
import com.apighost.model.scenario.step.Route;
import com.apighost.model.scenario.step.Then;
import com.apighost.parser.scenario.writer.JsonScenarioResultWriter;
import com.apighost.parser.scenario.writer.ScenarioResultWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Unit test for {@link JsonScenarioResultWriter}.
 * <p>
 * This test verifies the correct serialization of a {@link ScenarioResult} object to a JSON file
 * using {@link JsonScenarioResultWriter}.
 *
 * @author haazz
 * @version BETA-0.0.1
 */
class JsonScenarioResultWriterTest {

    /**
     * Output file path for the test result.
     */
    private static final String TEST_FILE_PATH = "test-scenario-result.json";

    /**
     * Clean-up method to delete the test output file after each test execution.
     */
    @AfterEach
    void tearDown() {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Tests that {@link JsonScenarioResultWriter} can serialize a fully populated
     * {@link ScenarioResult} object to a valid JSON file with correct content and structure.
     *
     * @throws Exception if file operations or serialization fails
     */
    @Test
    void testWriteScenarioResult_withBuilderModels_shouldWriteJsonFile() throws Exception {

        ScenarioResultWriter writer = new JsonScenarioResultWriter();
        ScenarioResult scenarioResult = new ScenarioResult.Builder()
            .name("Signup Scenario")
            .description("Validate new user registration and login process")
            .executedAt("2025-04-23T14:15:00.000")
            .totalDurationMs(550)
            .averageDurationMs(275)
            .filePath("/local/result/user")
            .baseUrl("http://localhost:8080")
            .isScenarioSuccess(true)
            .results(List.of(
                new ResultStep.Builder()
                    .stepName("signup")
                    .type(ProtocolType.HTTP)
                    .url("http://localhost:8080/api/signup")
                    .method(HTTPMethod.POST)
                    .requestBody(
                        new RequestBody.Builder()
                            .json("{"
                                + "\"email\": \"test@test.com\""
                                + "\"password\": \"1234\""
                                + "}")
                            .build()
                    )
                    .requestHeader(Map.of("Authorization", "Bearer abc.def.ghi", "Content-Type",
                        "application/json"))
                    .responseBody("\"status\" : \"success\"")
                    .responseHeaders(Map.of("Content-Type", "application/json"))
                    .status(200)
                    .startTime("2025-04-23T14:15:01.000")
                    .endTime("2025-04-23T14:15:01.300")
                    .durationMs(300)
                    .isRequestSuccess(true)
                    .route(
                        List.of(
                            new Route.Builder()
                                .expected(
                                    new Expected.Builder()
                                        .status("200")
                                        .value(Map.of("field", "value"))
                                        .build()
                                )
                                .then(
                                    new Then.Builder()
                                        .store(Map.of("postId", "123"))
                                        .step("createPost")
                                        .build()
                                )
                                .build()
                        )
                    )
                    .build()
            ))
            .build();

        writer.writeScenarioResult(scenarioResult, TEST_FILE_PATH);

        /** Assert: File is created */
        File outputFile = new File(TEST_FILE_PATH);
        assertTrue(outputFile.exists(), "Output file should be created");

        /** Assert: Raw JSON content contains important fields */
        String content = Files.readString(Path.of(TEST_FILE_PATH));
        assertTrue(content.contains("\"stepName\" : \"signup\""), "Step name should be in JSON");
        assertTrue(content.contains("\"filePath\" : \"/local/result/user\""),
            "filePath should be in JSON");
        assertTrue(content.contains("\"postId\""), "Saved variable 'postId' should exist in JSON");
    }
}
