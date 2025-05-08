package com.apighost.scenario.executor;

import com.apighost.model.scenario.Scenario;
import com.apighost.model.scenario.ScenarioResult;
import com.apighost.model.scenario.result.ResultStep;
import com.apighost.model.scenario.step.Step;
import com.apighost.scenario.callback.ScenarioResultCallback;
import com.apighost.validator.ScenarioValidator;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Executor responsible for running a full {@link Scenario} consisting of multiple steps.
 * <p>
 * Currently supports only HTTP-based steps via {@link HTTPStepExecutor}. WebSocket support is
 * planned.
 * </p>
 * <p>
 * Example:
 * <pre>{@code
 * ScenarioTestExecutor executor = new ScenarioTestExecutor();
 * ScenarioResult result = executor.execute(scenario, scenarioResultCallback);
 * }</pre>
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class ScenarioTestExecutor {

    StepExecutor httpStepExecutor = new HTTPStepExecutor();

    /**
     * Executes the provided scenario and returns the result.
     *
     * @param scenario the scenario to execute
     * @param callback optional callback invoked after each step and at the end of the scenario
     * @return the {@link ScenarioResult} containing execution outcomes
     */
    public ScenarioResult execute(Scenario scenario, ScenarioResultCallback callback) {
        ScenarioValidator.validateScenarioForExecution(scenario);
        ScenarioValidator.validateNoRouteCycle(scenario,
            scenario.getSteps().keySet().iterator().next());

        List<ResultStep> resultStepList = new ArrayList<>();
        Map<String, Object> store =
            scenario.getStore() != null ? scenario.getStore() : new HashMap<>();
        LinkedHashMap<String, Step> steps = scenario.getSteps();
        boolean isAllScenarioSuccess = true;
        long totalDurationMs = 0;

        String currentStepKey = steps.keySet().iterator().next();

        while (currentStepKey != null) {
            Step currentStep = steps.get(currentStepKey);
            long remainTimeoutMs = scenario.getTimeoutMs() - totalDurationMs;
            ResultStep resultStep;

            try {
                resultStep = switch (currentStep.getType()) {
                    case HTTP ->
                        httpStepExecutor.execute(currentStepKey, currentStep, store,
                            remainTimeoutMs);
                    case WEBSOCKET ->
                        throw new UnsupportedOperationException("WS not implemented yet");
                };
            } catch (Exception e) {
                resultStep = new ResultStep.Builder()
                    .stepName(currentStepKey)
                    .type(currentStep.getType())
                    .method(currentStep.getRequest().getMethod())
                    .url(currentStep.getRequest().getUrl())
                    .requestHeader(currentStep.getRequest().getHeader())
                    .requestBody(currentStep.getRequest().getBody())
                    .route(currentStep.getRoute())
                    .build();
            }

            if (!resultStep.getIsRequestSuccess()) {
                isAllScenarioSuccess = false;
            }

            resultStepList.add(resultStep);
            totalDurationMs += resultStep.getDurationMs();
            callback.onStepCompleted(currentStepKey, currentStep, resultStep);
            currentStepKey = resultStep.getNextStep();
        }
        ScenarioResult scenarioResult = new ScenarioResult.Builder()
            .name(scenario.getName())
            .description(scenario.getDescription())
            .executedAt(Instant.now().toString())
            .totalDurationMs(totalDurationMs)
            .averageDurationMs(
                resultStepList.isEmpty() ? 0 : totalDurationMs / resultStepList.size())
            .isScenarioSuccess(isAllScenarioSuccess)
            .results(resultStepList)
            .build();
        callback.onScenarioCompleted(scenario, scenarioResult);
        return scenarioResult;
    }
}
