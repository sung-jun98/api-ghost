package com.apighost.validator;

import com.apighost.model.scenario.Scenario;
import com.apighost.model.scenario.request.FormData;
import com.apighost.model.scenario.step.ProtocolType;
import com.apighost.model.scenario.step.Route;
import com.apighost.model.scenario.step.Step;
import com.apighost.model.scenario.step.Expected;
import com.apighost.model.scenario.step.Then;
import com.apighost.model.scenario.request.Request;
import com.apighost.model.scenario.request.RequestBody;

import com.apighost.util.file.FileType;
import com.apighost.util.file.FileUtil;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Validation for scenario test.
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class ScenarioValidator {

    /**
     * Validates a {@link Scenario} instance for structure and execution readiness.
     * <p>
     * <p>Validation rules include:</p>
     * <ul>
     *   <li>The scenario must not be null</li>
     *   <li>The scenario must contain at least one step</li>
     *   <li>Each step must have a valid type and a request with method and URL</li>
     *   <li>The request body must not contain both JSON and FormData</li>
     *   <li>Each route must include an expected condition (status or value)</li>
     *   <li>The {@link Then} step reference must exist in the scenario's step list</li>
     * </ul>
     *
     * @param scenario the scenario to validate
     * @throws IllegalArgumentException if any structural or logical inconsistency is found
     */
    public static void validateScenarioForExecution(Scenario scenario) {
        if (scenario == null) {
            throw new IllegalArgumentException("Scenario must not be null.");
        }

        LinkedHashMap<String, Step> steps = scenario.getSteps();
        if (steps == null || steps.isEmpty()) {
            throw new IllegalArgumentException("Scenario must contain at least one step.");
        }

        for (Map.Entry<String, Step> stepEntry : steps.entrySet()) {
            validateStepForExecution(steps, stepEntry);
        }
    }

    /**
     * Validates a single step and its associated request and routes.
     *
     * @param steps     all steps in the scenario
     * @param stepEntry the step entry to validate
     * @throws IllegalArgumentException if the step or its subcomponents are invalid
     */
    public static void validateStepForExecution(LinkedHashMap<String, Step> steps,
        Map.Entry<String, Step> stepEntry) {
        String stepKey = stepEntry.getKey();
        Step step = stepEntry.getValue();

        if (step == null) {
            throw new IllegalArgumentException("Step '" + stepKey + "' must not be null.");
        }

        if (step.getType() == null) {
            throw new IllegalArgumentException("Step '" + stepKey + "' must have a type.");
        }
        validateRequestForExecution(step, stepKey, step.getRequest());
        validateRouteForExecution(steps, stepKey, step.getRoute());
    }

    /**
     * Validates the {@link Request} object in a step, including method, URL, and ensuring that JSON
     * and FormData are not both present.
     *
     * @param step    the parent step
     * @param stepKey the identifier for the step
     * @param request the request to validate
     * @throws IllegalArgumentException if any request field is invalid
     */
    public static void validateRequestForExecution(Step step, String stepKey, Request request) {
        if (request == null) {
            throw new IllegalArgumentException("Step '" + stepKey + "' must have a request.");
        }

        if (ProtocolType.HTTP == step.getType() && request.getMethod() == null) {
            throw new IllegalArgumentException(
                "Step '" + stepKey + "' must have an HTTP method.");
        }

        if (request.getUrl() == null || request.getUrl().isEmpty()) {
            throw new IllegalArgumentException("Step '" + stepKey + "' must have a URL.");
        }

        RequestBody body = request.getBody();
        if (body != null) {
            if (body.getFormdata() != null && body.getJson() != null) {
                throw new IllegalArgumentException(
                    "Step '" + stepKey + "' cannot have both JSON and FormData.");
            }
        }
    }

    /**
     * Validates a list of {@link Route}s for a given step. Each route must contain an expected
     * condition with either a status or a value. The 'then' step must be specified and must exist
     * in the scenario.
     *
     * @param steps   all steps in the scenario
     * @param stepKey the step this route belongs to
     * @param routes  list of route objects to validate
     * @throws IllegalArgumentException if any route or reference is invalid
     */
    public static void validateRouteForExecution(LinkedHashMap<String, Step> steps, String stepKey,
        List<Route> routes) {

        if (routes == null || routes.isEmpty()) {
            throw new IllegalArgumentException(
                "Step '" + stepKey + "' must contain at least one route.");
        }

        for (Route route : routes) {
            if (route.getExpected() == null) {
                throw new IllegalArgumentException(
                    "Route in step '" + stepKey + "' must have an expected condition.");
            }

            Expected expected = route.getExpected();
            if ((expected.getStatus() == null || expected.getStatus().isEmpty()) && (
                expected.getValue() == null || expected.getValue().isEmpty())) {
                throw new IllegalArgumentException(
                    "Both expected status and value cannot be null in step '" + stepKey
                        + "'."
                );
            }

            Then then = route.getThen();
            if (then == null || then.getStep() == null || then.getStep().isEmpty()) {
                continue;
            }
            if (!steps.containsKey(then.getStep())) {
                throw new IllegalArgumentException(
                    "Then step '" + then.getStep() + "' in step '" + stepKey
                        + "' does not exist in scenario steps."
                );
            }
        }
    }

    /**
     * Validates that there are no cycles in the route graph of the given scenario. Starts from the
     * first declared step and performs a DFS.
     *
     * @param scenario     the scenario to validate
     * @param startStepKey the step key that starts the traversal
     * @throws IllegalArgumentException if a cycle is detected
     */
    public static void validateNoRouteCycle(Scenario scenario, String startStepKey) {
        if (scenario == null) {
            throw new IllegalArgumentException("Scenario must not be null.");
        }
        if (startStepKey == null) {
            throw new IllegalArgumentException("Step start key must not be null.");
        }

        LinkedHashMap<String, Step> steps = scenario.getSteps();
        if (steps == null || steps.isEmpty()) {
            throw new IllegalArgumentException("Scenario must contain at least one step.");
        }

        Set<String> visitedStepKeys = new HashSet<>();
        detectRouteCycle(steps, visitedStepKeys, startStepKey);
    }

    /**
     * Performs DFS traversal to detect cycles in the route graph. If a step is revisited while
     * still in the current path, a cycle exists.
     *
     * @param steps           all steps in the scenario
     * @param visitedStepKeys the set of currently visited steps in the DFS path
     * @param currentStepKey  the step key currently being visited
     * @throws IllegalArgumentException if a route cycle is detected
     */
    private static void detectRouteCycle(LinkedHashMap<String, Step> steps,
        Set<String> visitedStepKeys, String currentStepKey) {
        if (visitedStepKeys.contains(currentStepKey)) {
            throw new IllegalArgumentException("Cycle detected involving step: " + currentStepKey);
        }

        visitedStepKeys.add(currentStepKey);

        Step step = steps.get(currentStepKey);
        if (step == null || step.getRoute() == null) {
            visitedStepKeys.remove(currentStepKey);
            return;
        }

        for (Route route : step.getRoute()) {
            Then then = route.getThen();
            if (then == null || then.getStep() == null || then.getStep().isEmpty()) {
                return;
            }

            String nextStepKey = then.getStep();
            if (!steps.containsKey(nextStepKey)) {
                throw new IllegalArgumentException(
                    "Then step '" + nextStepKey + "' in step '" + currentStepKey
                        + "' does not exist in scenario steps."
                );
            }
            detectRouteCycle(steps, visitedStepKeys, nextStepKey);
        }

        visitedStepKeys.remove(currentStepKey);
    }

    /**
     * Validates the {@link FormData} fields (both text and file) in a scenario step.
     * <p>
     * This method performs basic checks on form field names and values:
     * <ul>
     *     <li>Text fields: verifies non-null names and logs warnings for missing values.</li>
     *     <li>File fields: checks for non-null field names and file names.</li>
     *     <li>Attempts to locate each file in the resources directory and logs if missing.</li>
     * </ul>
     * <p>
     * Warnings are logged using {@code System.out.println} instead of throwing exceptions,
     * allowing validation to proceed without interruption for missing or invalid files.
     *
     * @param stepKey  the identifier of the step this form data belongs to
     * @param formData the {@link FormData} object containing text and file fields to validate
     */
    private static void validateFormDataParts(String stepKey, FormData formData) {
        if (formData == null) {
            System.out.println("No FormData to validate in step: " + stepKey);
            return;
        }

        if (formData.getText() != null) {
            for (Map.Entry<String, String> textEntry : formData.getText().entrySet()) {
                String name = textEntry.getKey();
                String value = textEntry.getValue();
                if (name == null || name.isEmpty()) {
                    System.out.println("Warning: Text field name is empty in step: " + stepKey);
                    continue;
                }
                if (value == null) {
                    System.out.println("Warning: Text field value is null for name '" + name + "' in step: " + stepKey);
                }
                System.out.println("Validated text field: " + name + " in step: " + stepKey);
            }
        }

        if (formData.getFile() != null) {
            for (Map.Entry<String, String> fileEntry : formData.getFile().entrySet()) {
                String name = fileEntry.getKey();
                String fileName = fileEntry.getValue();
                if (name == null || name.isEmpty()) {
                    System.out.println("Warning: File field name is empty in step: " + stepKey);
                    continue;
                }
                if (fileName == null || fileName.isEmpty()) {
                    System.out.println("Warning: File name is empty for name '" + name + "' in step: " + stepKey);
                    continue;
                }
                Path baseDir = FileUtil.findDirectory(FileType.RESOURCES, null);
                Path filePath = baseDir.resolve(fileName);
                System.out.println("Checking file: " + filePath + " for name '" + name + "' in step: " + stepKey);
                if (!Files.exists(filePath)) {
                    System.out.println("Warning: File not found: " + filePath + ". Proceeding with empty content.");
                }
            }
        }
    }

}
