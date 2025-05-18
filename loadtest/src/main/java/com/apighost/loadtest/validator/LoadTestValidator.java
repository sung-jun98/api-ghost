package com.apighost.loadtest.validator;

import com.apighost.loadtest.parameter.StageExecuteParameter;
import com.apighost.loadtest.parameter.StageExecuteParameterResolver;
import com.apighost.model.loadtest.parameter.LoadTestExecuteParameter;
import com.apighost.model.scenario.Scenario;
import com.apighost.validator.ScenarioValidator;

/**
 * Utility class for validating parameters used in load test execution.
 * <p>
 * Provides static methods to validate the integrity and correctness of
 * {@link LoadTestExecuteParameter} and {@link StageExecuteParameter} objects before they are used
 * in test execution.
 * </p>
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class LoadTestValidator {

    /**
     * Validates the given {@link LoadTestExecuteParameter} instance to ensure it is properly
     * configured.
     *
     * <p>This method checks for null references, missing names, empty stage lists,
     * and empty scenario definitions.</p>
     *
     * @param parameter the {@code LoadTestExecuteParameter} to validate
     * @throws IllegalArgumentException if the parameter or any of its critical fields are invalid
     */
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
            throw new IllegalArgumentException(
                "LoadTestParameter scenarios cannot be null or empty");
        }
    }

    /**
     * Validates the given {@link StageExecuteParameter} instance to ensure all required parameters
     * for stage execution are correctly set.
     *
     * <p>This includes checking that the virtual user counts and duration values are non-negative,
     * that increment interval meets minimum thresholds, and that all scenarios are valid for
     * execution using {@link ScenarioValidator}.</p>
     *
     * @param parameter the {@code StageExecuteParameter} to validate
     * @throws IllegalArgumentException if any parameter is invalid or missing
     */
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

        if (parameter.getIncrementIntervalMs()
            < StageExecuteParameterResolver.DEFAULT_INCREMENT_INTERVAL_MS) {
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
