package parser.scenario.writer;

import com.apighost.model.scenario.HTTPMethod;
import com.apighost.model.scenario.ProtocolType;
import com.apighost.model.scenario.result.*;
import com.apighost.parser.scenario.writer.JsonScenarioResultWriter;
import com.apighost.parser.scenario.writer.ScenarioResultWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
            .scenarioId("SC-001")
            .name("Signup Scenario")
            .description("Validate new user registration and login process")
            .executedAt("2025-04-23T14:15:00.000")
            .totalDurationMs(550)
            .averageDurationMs(275)
            .filePath("/local/result/user")
            .baseUrl("http://localhost:8080")
            .isScenarioSuccess(true)
            .results(List.of(
                new StepResult.Builder()
                    .stepName("signup")
                    .type(ProtocolType.HTTP)
                    .url("http://localhost:8080/api/signup")
                    .method(HTTPMethod.POST)
                    .requestBody(Map.of("email", "test@test.com", "password", "1234"))
                    .requestHeader(Map.of("Authorization", "Bearer abc.def.ghi", "Content-Type",
                        "application/json"))
                    .responseBody(Map.of("status", "success"))
                    .responseHeaders(Map.of("Content-Type", "application/json"))
                    .status(200)
                    .startTime("2025-04-23T14:15:01.000")
                    .endTime("2025-04-23T14:15:01.300")
                    .durationMs(300)
                    .requestSuccess(true)
                    .response(List.of(
                        new ResponseBranch.Builder()
                            .when(new WhenCondition.Builder()
                                .status("200")
                                .body(Map.of("field", "value"))
                                .condition("${response.body.posts.length} == 0")
                                .build())
                            .then(new ThenAction.Builder()
                                .save(Map.of("postId", "123"))
                                .next("create_post")
                                .build())
                            .build()
                    ))
                    .build()
            ))
            .build();

        writer.writeScenarioResult(scenarioResult, TEST_FILE_PATH);

        /** Assert: File is created */
        File outputFile = new File(TEST_FILE_PATH);
        assertTrue(outputFile.exists(), "Output file should be created");

        /** Assert: Raw JSON content contains important fields */
        String content = Files.readString(Path.of(TEST_FILE_PATH));
        assertTrue(content.contains("\"scenarioId\" : \"SC-001\""),
            "Scenario ID should be in JSON");
        assertTrue(content.contains("\"stepName\" : \"signup\""), "Step name should be in JSON");
        assertTrue(content.contains("\"postId\""), "Saved variable 'postId' should exist in JSON");
    }
}
