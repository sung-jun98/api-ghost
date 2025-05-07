package com.apighost.scenario.callback;

import com.apighost.model.scenario.Scenario;
import com.apighost.model.scenario.ScenarioResult;
import com.apighost.model.scenario.result.ResultStep;
import com.apighost.model.scenario.step.Step;

/**
 * Callback interface for receiving events during scenario execution.
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public interface ScenarioResultCallback {

    /**
     * Called after an individual step has completed.
     *
     * @param stepId     the identifier of the completed step
     * @param step       the step that was executed
     * @param resultStep the result of the step execution
     */
    default void onStepCompleted(String stepId, Step step, ResultStep resultStep) {
        // default: do nothing
    }

    /**
     * Called after the entire scenario has finished executing.
     *
     * @param scenario       the scenario that was executed
     * @param scenarioResult the result of the entire scenario execution
     */
    default void onScenarioCompleted(Scenario scenario, ScenarioResult scenarioResult) {
        // default: do nothing
    }
}
