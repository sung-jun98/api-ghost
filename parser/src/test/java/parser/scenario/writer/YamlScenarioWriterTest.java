package parser.scenario.writer;

import com.apighost.model.scenario.request.*;
import com.apighost.model.scenario.step.*;
import com.apighost.model.scenario.Scenario;
import com.apighost.model.scenario.ScenarioResult;
import com.apighost.parser.scenario.writer.JsonScenarioResultWriter;
import com.apighost.parser.scenario.writer.ScenarioWriter;
import com.apighost.parser.scenario.writer.YamlScenarioWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link YamlScenarioWriter#writeScenario(Scenario)}.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class YamlScenarioWriterTest {

    /**
     * Scenario Dto -> YAML file test
     *
     * @throws IOException File Input error
     */
    @Test
    void writeFormdataScenario() throws IOException {
        Scenario INPUT_FORMDATA_VER = createMockFormdataScenario();
        Scenario INPUT_JSON_VER = createMockJsonScenario();
        ScenarioWriter writer = new YamlScenarioWriter();

        /** Depending on the situation, you can choose one of the two cases to test it. */
        writer.writeScenario(INPUT_FORMDATA_VER);
        writer.writeScenario(INPUT_JSON_VER);

    }

    /**
     * Create a dummy for testing scenario dto(FomrData Version)
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
        Request request = new Request.Builder().method("string").url("string").header(header)
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
        Position position = new Position.Builder().x(101.0).y(200.0).build();

        Step step = new Step.Builder().type("string").position(position).request(request)
                        .route(List.of(route)).build();

        Map<String, Step> steps = new HashMap<>();
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
                              .method("string")
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
                        .type("string")
                        .position(position)
                        .request(request)
                        .route(List.of(route))
                        .build();

        Map<String, Step> steps = new HashMap<>();
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
}
