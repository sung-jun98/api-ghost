package com.apighost.loadtest.validator;

import com.apighost.loadtest.parameter.StageExecuteParameter;
import com.apighost.loadtest.parameter.StageExecuteParameterResolver;
import com.apighost.model.loadtest.parameter.LoadTestExecuteParameter;
import com.apighost.model.scenario.Scenario;
import com.apighost.validator.ScenarioValidator;

public class LoadTestValidator {

    public static void validateLoadTestExecuteParameter(LoadTestExecuteParameter parameter) {

        if (parameter == null) {
            throw new IllegalArgumentException("LoadTestParameter cannot be null");
        }

        if (parameter.getName() == null) {
            throw new IllegalArgumentException("LoadTestParameter name cannot be null");
        }

        if (parameter.getStages() == null || parameter.getStages().isEmpty()) {
            throw new IllegalArgumentException("LoadTestParameter stages cannot be null or empty");
        }

        if (parameter.getScenarios() == null || parameter.getScenarios().isEmpty()) {
            throw new IllegalArgumentException("LoadTestParameter scenarios cannot be null or empty");
        }
    }

    public static void validateStageExecutorParameter(StageExecuteParameter parameter) {

        if (parameter == null) {
            throw new IllegalArgumentException("StageExecuteParameter cannot be null");
        }

        if (parameter.getStartVus() < 0 || parameter.getTargetVus() < 0) {
            throw new IllegalArgumentException("VUs must be greater than zero");
        }

        if (parameter.getTotalDurationMs() < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero");
        }

        if (parameter.getThinkTimeMs() < 0) {
            throw new IllegalArgumentException("ThinkTime must be greater than zero");
        }

        if (parameter.getIncrementIntervalMs() < StageExecuteParameterResolver.DEFAULT_INCREMENT_INTERVAL_MS) {
            throw new IllegalArgumentException(
                "incrementIntervalMs must be >= " +
                    StageExecuteParameterResolver.DEFAULT_INCREMENT_INTERVAL_MS
            );
        }

        if (parameter.getScenarios() == null) {
            throw new IllegalArgumentException("Missing scenarios");
        }

        for (Scenario scenario : parameter.getScenarios()) {
            ScenarioValidator.validateScenarioForExecution(scenario);
        }
    }
}
