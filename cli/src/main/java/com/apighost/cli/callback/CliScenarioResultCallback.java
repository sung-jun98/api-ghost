package com.apighost.cli.callback;

import com.apighost.cli.util.ConsoleOutput;
import com.apighost.model.scenario.Scenario;
import com.apighost.model.scenario.ScenarioResult;
import com.apighost.model.scenario.result.ResultStep;
import com.apighost.model.scenario.step.ProtocolType;
import com.apighost.model.scenario.step.Step;
import com.apighost.scenario.callback.ScenarioResultCallback;

/**
 * CLI-specific implementation of {@link ScenarioResultCallback} to display test step results and
 * overall scenario execution summary to the console.
 *
 * <p>This implementation prints formatted information for each step's result and the final
 * scenario outcome using the {@link ConsoleOutput} utility. It supports both HTTP and WebSocket
 * protocols.</p>
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class CliScenarioResultCallback implements ScenarioResultCallback {

    /**
     * Prints the result of a single step to the console.
     *
     * @param stepId the identifier of the completed step
     * @param step   the step definition
     * @param result the execution result of the step
     */
    @Override
    public void onStepCompleted(String stepId, Step step, ResultStep result) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[STEP] ").append(stepId).append("\n");

        stringBuilder.append("-> ").append(result.getType()).append(" ")
            .append((result.getType() == ProtocolType.HTTP) ? result.getMethod() : "").append(" ")
            .append(result.getUrl()).append("\n");

        stringBuilder.append("-> ").append(result.getIsRequestSuccess() ? "Success" : "Failure")
            .append(" ")
            .append(result.getStatus()).append(" ")
            .append(result.getDurationMs()).append(" ms\n");
        stringBuilder.append("\n");
        ConsoleOutput.print(stringBuilder.toString());
    }

    /**
     * Prints the result of scenario to the console.
     *
     * @param scenario the identifier of the completed step
     * @param result   the execution result of the scenario
     */
    @Override
    public void onScenarioCompleted(Scenario scenario, ScenarioResult result) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Executed at     : ").append(result.getExecutedAt()).append("\n");
        stringBuilder.append("Total duration  : ").append(result.getTotalDurationMs())
            .append(" ms\n");
        stringBuilder.append("Average per step: ").append(result.getAverageDurationMs())
            .append(" ms\n");
        stringBuilder.append("Scenario success: ")
            .append(result.getIsScenarioSuccess() ? "PASS" : "FAIL").append("\n");

        ConsoleOutput.print(stringBuilder.toString());
    }
}
