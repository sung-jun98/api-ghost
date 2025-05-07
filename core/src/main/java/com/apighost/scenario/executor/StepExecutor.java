package com.apighost.scenario.executor;

import com.apighost.model.scenario.result.ResultStep;
import com.apighost.model.scenario.step.Step;
import java.io.IOException;
import java.util.Map;

/**
 * Interface for executing a single step within a scenario.
 * <p>
 * Implementations of this interface are responsible for performing protocol-specific
 * operations (e.g., HTTP, WebSocket) based on the provided {@link Step} definition.
 * </p>
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public interface StepExecutor {
    /**
     * Executes a single step within a scenario.
     *
     * @param stepKey   the unique identifier of the step in the scenario
     * @param step      the {@link Step} object defining the execution logic
     * @param store     a shared key-value store for data persistence across steps (e.g., extracted variables)
     * @param timeoutMs maximum allowed execution time in milliseconds for this step
     * @return {@link ResultStep} containing the outcome of the step execution
     * @throws IOException          if an I/O error occurs during step execution
     * @throws InterruptedException if the execution is interrupted (e.g., due to timeout or thread interruption)
     */
    ResultStep execute(String stepKey, Step step, Map<String, Object> store, long timeoutMs) throws IOException, InterruptedException;
}