package com.apighost.web.sse;

import com.apighost.model.scenario.Scenario;
import com.apighost.model.scenario.ScenarioResult;
import com.apighost.model.scenario.result.ResultStep;
import com.apighost.model.scenario.step.Step;
import com.apighost.scenario.callback.ScenarioResultCallback;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * ScenarioTestSSECallback is an implementation of the ScenarioResultCallback interface
 * designed to handle server-sent events (SSE) related to the completion of steps and scenarios
 * in a test execution. It utilizes SSEManager to send real-time updates to a client when specific
 * events occur during the scenario execution.
 *
 * The callback performs the following tasks:
 * - Sends step completion data as a server-sent event to the client.
 * - Sends scenario completion data as a server-sent event to the client.
 * - Initiates the removal and closure of the client connection after a delay following
 *   scenario completion.
 *
 * This class employs ObjectMapper for serializing Java objects (e.g., ResultStep and ScenarioResult)
 * into JSON for transmission over SSE.
 *
 * Threading is used during client connection closure to allow the client sufficient time to process
 * the "complete" event.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class ScenarioTestSSECallback implements ScenarioResultCallback {

    private final String clientId;
    private final SSEManager sseManager;
    private final ObjectMapper objectMapper;

    public ScenarioTestSSECallback(String clientId, SSEManager sseManager) {
        this.clientId = clientId;
        this.sseManager = sseManager;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void onStepCompleted(String currentStepKey, Step currentStep, ResultStep resultStep) {
        try {
            String resultJson = objectMapper.writeValueAsString(resultStep);
            sseManager.sendEvent(clientId, "stepResult", resultJson);
        } catch (IOException e) {
            System.err.println("Error occurs during SSE event transmission: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onScenarioCompleted(Scenario scenario, ScenarioResult scenarioResult) {
        try {
            String resultJson = objectMapper.writeValueAsString(scenarioResult);
            sseManager.sendEvent(clientId, "complete", resultJson);

            /** Close the connection after a short delay (to allow the client to process the event) */
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    SSEClient client = sseManager.removeClient(clientId);
                    if (client != null) {
                        client.close();
                    }
                } catch (InterruptedException | IOException e) {
                    System.err.println("Error occurs during the termination of SSE connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            System.err.println("Error occurs during SSE event transmission: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
