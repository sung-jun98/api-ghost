package parser.scenario.writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apighost.model.scenario.request.*;
import com.apighost.model.scenario.step.*;
import com.apighost.model.scenario.Scenario;
import com.apighost.parser.scenario.reader.ScenarioReader;
import com.apighost.parser.scenario.reader.YamlScenarioReader;
import com.apighost.parser.scenario.writer.ScenarioWriter;
import com.apighost.parser.scenario.writer.YamlScenarioWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link YamlScenarioWriter}
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class YamlScenarioWriterTest {

    /**
     * Output file path for the test result.
     */
    private static final String TEST_FILE_PATH = "test-scenario-result.yaml";
    private static ObjectMapper objectMapper = new ObjectMapper();

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
     * Scenario Dto -> YAML file test
     *
     * @throws IOException File Input error
     */
    @Test
    void writeFormdataScenario() throws IOException {
        Scenario expectedScenario = createMockJsonScenarioWithFormData();

        ScenarioWriter writer = new YamlScenarioWriter();
        ScenarioReader reader = new YamlScenarioReader();

        /** Depending on the situation, you can choose one of the two cases to test it. */
        writer.writeScenario(expectedScenario, TEST_FILE_PATH);

        /** Then: Verify file creation */
        File outputFile = new File(TEST_FILE_PATH);
        assertTrue(outputFile.exists(), "YAML file should be created");

        /** Then: Read and compare scenarios */
        Scenario actualScenario = reader.readScenario(TEST_FILE_PATH);
        String jsonOutput = objectMapper.writerWithDefaultPrettyPrinter()
                                .writeValueAsString(actualScenario);

        /** Verify basic fields */
        assertEquals(expectedScenario.getName(), actualScenario.getName(),
            "Scenario name should match");
        assertEquals(expectedScenario.getDescription(), actualScenario.getDescription(),
            "Description should match");
        assertEquals(expectedScenario.getTimeoutMs(), actualScenario.getTimeoutMs(),
            "Timeout should match");

        /** Verify store values */
        assertEquals(expectedScenario.getStore(), actualScenario.getStore(),
            "Store values should match");

        /** Verify steps */
        Map<String, Step> expectedSteps = expectedScenario.getSteps();
        Map<String, Step> actualSteps = actualScenario.getSteps();
        assertEquals(expectedSteps.size(), actualSteps.size(), "Number of steps should match");

        /** Verify step details */
        Step expectedStep = expectedSteps.get("stepName");
        Step actualStep = actualSteps.get("stepName");
        assertEquals(expectedStep.getType(), actualStep.getType(), "Step type should match");

        /** Verify position */
        assertEquals(expectedStep.getPosition().getX(), actualStep.getPosition().getX(),
            "Position X should match");
        assertEquals(expectedStep.getPosition().getY(), actualStep.getPosition().getY(),
            "Position Y should match");

        /** Verify request */
        Request expectedRequest = expectedStep.getRequest();
        Request actualRequest = actualStep.getRequest();
        assertEquals(expectedRequest.getMethod(), actualRequest.getMethod(),
            "Request method should match");
        assertEquals(expectedRequest.getUrl(), actualRequest.getUrl(), "Request URL should match");
        assertEquals(expectedRequest.getHeader(), actualRequest.getHeader(),
            "Request headers should match");

        /** Verify request body */
        assertEquals(expectedRequest.getBody().getJson(), actualRequest.getBody().getJson(),
            "Request body should match");

        /** Verify routes */
        List<Route> expectedRoutes = expectedStep.getRoute();
        List<Route> actualRoutes = actualStep.getRoute();
        assertEquals(expectedRoutes.size(), actualRoutes.size(), "Number of routes should match");

        /** Verify route details */
        Route expectedRoute = expectedRoutes.get(0);
        Route actualRoute = actualRoutes.get(0);

        /** Verify expected values in route */
        assertEquals(expectedRoute.getExpected().getStatus(), actualRoute.getExpected().getStatus(),
            "Expected status should match");
        assertEquals(expectedRoute.getExpected().getValue(), actualRoute.getExpected().getValue(),
            "Expected values should match");

        /** Verify then values in route */
        assertEquals(expectedRoute.getThen().getStep(), actualRoute.getThen().getStep(),
            "Then step should match");
        assertEquals(expectedRoute.getThen().getStore(), actualRoute.getThen().getStore(),
            "Then store values should match");
    }

    /**
     * Create a dummy for testing scenario dto (FomrData Version)
     *
     * @return Scenario
     */
    static Scenario createMockFormdataScenario() {
        /** store */
        Map<String, Object> store = new HashMap<>();
        store.put("key", "value");

        /** header */
        Map<String, String> header = new HashMap<>();
        header.put("headerName", "value");
        header.put("destination", "string");
        header.put("subscription", "string");
        header.put("message", "string");
        header.put("action", "string");

        /** request body */
        RequestBody requestBody = new RequestBody.Builder().formdata(null).json(
                "{\"userId\": \"user_123456\",\"preferences\": {\"language\": \"ko\",\"notifications\": [{\"email\": true,\"sms\": false,\"push\": {\"enabled\": true,\"frequency\": \"daily\"}}]}}")
                                      .build();

        /** request */
        Request request = new Request.Builder().method(HTTPMethod.POST).url("string").header(header)
                              .body(requestBody).build();

        /** expected */
        Map<String, Object> expectedValue = new HashMap<>();
        expectedValue.put("fieldName", "value");
        Expected expected = new Expected.Builder().status("200").value(expectedValue).build();

        /** then */
        Map<String, Object> thenStore = new HashMap<>();
        thenStore.put("variableName", "value");
        Then then = new Then.Builder().store(thenStore).step("string").build();

        /** route */
        Route route = new Route.Builder().expected(expected).then(then).build();

        /** step */
        Position position = new Position.Builder().x(105.0).y(200.0).build();

        Step step = new Step.Builder().type(ProtocolType.HTTP).position(position).request(request)
                        .route(List.of(route)).build();

        LinkedHashMap<String, Step> steps = new LinkedHashMap<>();
        steps.put("stepName", step);

        /** scenario */
        Scenario scenario = new Scenario.Builder().name("string").description("string")
                                .timeoutMs(1000L).store(store).steps(steps).build();

        return scenario;
    }

    /**
     * Create a dummy for testing scenario dto(Json Version)
     *
     * @return Scenario
     */
    static Scenario createMockJsonScenario() {
        Map<String, Object> store = new HashMap<>();
        store.put("key", "value");

        Position position = new Position.Builder()
                                .x(100.0)
                                .y(200.0)
                                .build();

        Map<String, String> header = new HashMap<>();
        header.put("headerName", "value");
        header.put("destination", "string");
        header.put("subscription", "string");
        header.put("message", "string");
        header.put("action", "string");

        String jsonBody = """
            {
              "userId": "user_123456",
              "preferences": {
                "language": "ko",
                "notifications": [{
                  "email": true,
                  "sms": false,
                  "push": {
                    "enabled": true,
                    "frequency": "daily"
                  }
                }]
              }
            }
            """;
        RequestBody requestBody = new RequestBody.Builder()
                                      .json(jsonBody)
                                      .build();

        Request request = new Request.Builder()
                              .method(HTTPMethod.POST)
                              .url("string")
                              .header(header)
                              .body(requestBody)
                              .build();

        Map<String, Object> expectedValue = new HashMap<>();
        expectedValue.put("fieldName", "value");
        Expected expected = new Expected.Builder()
                                .status("200")
                                .value(expectedValue)
                                .build();

        Map<String, Object> thenStore = new HashMap<>();
        thenStore.put("variableName", "value");
        Then then = new Then.Builder()
                        .store(thenStore)
                        .step("string")
                        .build();

        Route route = new Route.Builder()
                          .expected(expected)
                          .then(then)
                          .build();

        Step step = new Step.Builder()
                        .type(ProtocolType.HTTP)
                        .position(position)
                        .request(request)
                        .route(List.of(route))
                        .build();

        LinkedHashMap<String, Step> steps = new LinkedHashMap<>();
        steps.put("stepName", step);

        Scenario scenario = new Scenario.Builder()
                                .name("string")
                                .description("string")
                                .timeoutMs(1000L)
                                .store(store)
                                .steps(steps)
                                .build();

        return scenario;
    }

    static Scenario createMockJsonScenarioWithFormData() {
        Map<String, Object> store = new HashMap<>();
        store.put("key", "value");

        /** First Step (Formdata version) */
        Position position1 = new Position.Builder()
                                 .x(105.0)
                                 .y(200.0)
                                 .build();

        Map<String, String> header1 = new HashMap<>();
        header1.put("headerName", "value");
        header1.put("destination", "string");
        header1.put("subscription", "string");
        header1.put("message", "string");
        header1.put("action", "string");

        RequestBody requestBody1 = new RequestBody.Builder()
                                       .formdata(null)
                                       .json(
                                           "{\"userId\": \"user_123456\",\"preferences\": {\"language\": \"ko\",\"notifications\": [{\"email\": true,\"sms\": false,\"push\": {\"enabled\": true,\"frequency\": \"daily\"}}]}}")
                                       .build();

        Request request1 = new Request.Builder()
                               .method(HTTPMethod.POST)
                               .url("string")
                               .header(header1)
                               .body(requestBody1)
                               .build();

        Map<String, Object> expectedValue1 = new HashMap<>();
        expectedValue1.put("fieldName", "value");
        Expected expected1 = new Expected.Builder()
                                 .status("200")
                                 .value(expectedValue1)
                                 .build();

        Map<String, Object> thenStore1 = new HashMap<>();
        thenStore1.put("variableName", "value");
        Then then1 = new Then.Builder()
                         .store(thenStore1)
                         .step("string")
                         .build();

        Route route1 = new Route.Builder()
                           .expected(expected1)
                           .then(then1)
                           .build();

        Step step1 = new Step.Builder()
                         .type(ProtocolType.HTTP)
                         .position(position1)
                         .request(request1)
                         .route(List.of(route1))
                         .build();

        /** Second Step (JSON version) */
        Position position2 = new Position.Builder()
                                 .x(100.0)
                                 .y(200.0)
                                 .build();

        Map<String, String> header2 = new HashMap<>();
        header2.put("headerName", "value");
        header2.put("destination", "string");
        header2.put("subscription", "string");
        header2.put("message", "string");
        header2.put("action", "string");

        String jsonBody = """
            {
              "userId": "user_123456",
              "preferences": {
                "language": "ko",
                "notifications": [{
                  "email": true,
                  "sms": false,
                  "push": {
                    "enabled": true,
                    "frequency": "daily"
                  }
                }]
              }
            }
            """;

        RequestBody requestBody2 = new RequestBody.Builder()
                                       .json(jsonBody)
                                       .build();

        Request request2 = new Request.Builder()
                               .method(HTTPMethod.POST)
                               .url("string")
                               .header(header2)
                               .body(requestBody2)
                               .build();

        Map<String, Object> expectedValue2 = new HashMap<>();
        expectedValue2.put("fieldName", "value");
        Expected expected2 = new Expected.Builder()
                                 .status("200")
                                 .value(expectedValue2)
                                 .build();

        Map<String, Object> thenStore2 = new HashMap<>();
        thenStore2.put("variableName", "value");
        Then then2 = new Then.Builder()
                         .store(thenStore2)
                         .step("string")
                         .build();

        Route route2 = new Route.Builder()
                           .expected(expected2)
                           .then(then2)
                           .build();

        Step step2 = new Step.Builder()
                         .type(ProtocolType.HTTP)
                         .position(position2)
                         .request(request2)
                         .route(List.of(route2))
                         .build();

        /** Combine steps */
        LinkedHashMap<String, Step> steps = new LinkedHashMap<>();
        steps.put("stepName", step2);
        steps.put("formdataStep", step1);

        /** Create scenario with both steps */
        return new Scenario.Builder()
                   .name("string")
                   .description("string")
                   .timeoutMs(1000L)
                   .store(store)
                   .steps(steps)
                   .build();
    }
}
