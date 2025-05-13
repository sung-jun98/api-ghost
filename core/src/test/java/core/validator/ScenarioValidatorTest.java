package core.validator;

import com.apighost.model.scenario.Scenario;
import com.apighost.model.scenario.step.*;
import com.apighost.model.scenario.request.*;
import com.apighost.validator.ScenarioValidator;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ScenarioValidator} class.
 * <p>
 * This test suite validates that scenarios, steps, requests, and routing logic are correctly
 * validated before execution. It covers error handling, invalid configurations, and edge cases such
 * as cycles.
 * </p>
 *
 * @author haazz
 * @version BETA-0.0.1
 */
class ScenarioValidatorTest {

    /**
     * Tests that passing a null scenario to the validator results in an
     * {@link IllegalArgumentException}.
     */
    @Test
    void testValidateScenarioForExecution_nullScenario_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            ScenarioValidator.validateScenarioForExecution(null);
        });
    }

    /**
     * Tests that a scenario with no steps results in an {@link IllegalArgumentException}.
     */
    @Test
    void testValidateScenarioForExecution_emptySteps_throwsException() {
        Scenario scenario = new Scenario.Builder().steps(new LinkedHashMap<>()).build();
        assertThrows(IllegalArgumentException.class, () -> {
            ScenarioValidator.validateScenarioForExecution(scenario);
        });
    }

    /**
     * Tests that a step missing its request object fails validation.
     */
    @Test
    void testValidateStepForExecution_missingRequest_throwsException() {
        Step invalidStep = new Step.Builder().type(ProtocolType.HTTP).build();
        LinkedHashMap<String, Step> steps = new LinkedHashMap<>();
        steps.put("step1", invalidStep);

        assertThrows(IllegalArgumentException.class, () -> {
            ScenarioValidator.validateStepForExecution(steps, steps.entrySet().iterator().next());
        });
    }

    /**
     * Tests that providing both JSON and FormData in a request body causes an
     * {@link IllegalArgumentException} due to invalid configuration.
     */
    @Test
    void testValidateRequestForExecution_bothJsonAndFormData_throwsException() {
        RequestBody body = new RequestBody.Builder()
            .json("{ \"key\": \"value\" }")
            .formdata(new FormData.Builder()
                .text(null)
                .build())
            .build();

        Request request = new Request.Builder()
            .method(HTTPMethod.POST)
            .url("https://example.com")
            .body(body)
            .build();

        Step step = new Step.Builder().type(ProtocolType.HTTP).build();

        assertThrows(IllegalArgumentException.class, () -> {
            ScenarioValidator.validateRequestForExecution(step, "step1", request);
        });
    }

    /**
     * Tests that referencing a non-existent step in a route's `then` clause results in an
     * {@link IllegalArgumentException}.
     */
    @Test
    void testValidateRouteForExecution_thenStepNotInSteps_throwsException() {
        LinkedHashMap<String, Step> steps = new LinkedHashMap<>();
        steps.put("step1", new Step.Builder()
            .type(ProtocolType.HTTP)
            .request(new Request.Builder().method(HTTPMethod.GET).url("https://a.com").build())
            .build());

        Route route = new Route.Builder()
            .expected(new Expected.Builder().status("200").build())
            .then(new Then.Builder().step("nonExistentStep").build())
            .build();

        assertThrows(IllegalArgumentException.class, () -> {
            ScenarioValidator.validateRouteForExecution(steps, "step1", List.of(route));
        });
    }

    /**
     * Tests that a cyclic reference between steps via route `then` clauses is detected and results
     * in an {@link IllegalArgumentException}.
     */
    @Test
    void testValidateNoRouteCycle_cycleDetected_throwsException() {
        Step stepA = new Step.Builder()
            .type(ProtocolType.HTTP)
            .request(new Request.Builder().method(HTTPMethod.GET).url("https://a.com").build())
            .route(List.of(new Route.Builder()
                .expected(new Expected.Builder().status("200").build())
                .then(new Then.Builder().step("stepB").build())
                .build()))
            .build();

        Step stepB = new Step.Builder()
            .type(ProtocolType.HTTP)
            .request(new Request.Builder().method(HTTPMethod.GET).url("https://b.com").build())
            .route(List.of(new Route.Builder()
                .expected(new Expected.Builder().status("200").build())
                .then(new Then.Builder().step("stepA").build()) // cycle
                .build()))
            .build();

        LinkedHashMap<String, Step> steps = new LinkedHashMap<>();
        steps.put("stepA", stepA);
        steps.put("stepB", stepB);

        Scenario scenario = new Scenario.Builder()
            .name("Cycle Scenario")
            .steps(steps)
            .build();

        assertThrows(IllegalArgumentException.class, () -> {
            ScenarioValidator.validateNoRouteCycle(scenario, "stepA");
        });
    }

    /**
     * Tests that a scenario with valid routing and no cycles passes validation successfully.
     */
    @Test
    void testValidateNoRouteCycle_noCycle_passes() {
        Step stepA = new Step.Builder()
            .type(ProtocolType.HTTP)
            .request(new Request.Builder().method(HTTPMethod.GET).url("https://a.com").build())
            .route(List.of(new Route.Builder()
                .expected(new Expected.Builder().status("200").build())
                .then(new Then.Builder().step("stepB").build())
                .build()))
            .build();

        Step stepB = new Step.Builder()
            .type(ProtocolType.HTTP)
            .request(new Request.Builder().method(HTTPMethod.GET).url("https://b.com").build())
            .route(List.of(new Route.Builder()
                .expected(new Expected.Builder().status("200").build())
                .then(new Then.Builder().step(null).build())
                .build()))
            .build();

        LinkedHashMap<String, Step> steps = new LinkedHashMap<>();
        steps.put("stepA", stepA);
        steps.put("stepB", stepB);

        Scenario scenario = new Scenario.Builder().steps(steps).build();

        assertDoesNotThrow(() -> {
            ScenarioValidator.validateNoRouteCycle(scenario, "stepA");
        });
    }
}
