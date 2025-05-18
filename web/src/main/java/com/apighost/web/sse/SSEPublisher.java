package com.apighost.web.sse;

import com.apighost.loadtest.publisher.ResultPublisher;
import com.apighost.model.loadtest.result.LoadTestSnapshot;
import com.apighost.model.loadtest.result.LoadTestSummary;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Scheduler run by Loadtestorchestrator empty the internal data of the buffer every time (5
 * seconds). The data inside the buffer is sent by Publisher to SSE or CLI, and the Publisher is
 * Publisher used to send to GUI to SSE.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class SSEPublisher implements ResultPublisher {

    private final SSEManager sseManager;
    private final ObjectMapper objectMapper;

    public SSEPublisher(HttpServletResponse response) throws IOException {
        this.sseManager = SSEManager.getInstance();
        this.objectMapper = new ObjectMapper();

        try {
            String clientId = UUID.randomUUID().toString();
            SSEClient sseClient = new SSEClient(clientId, response);
            sseManager.registerClient(clientId, sseClient);

        } catch (IOException e) {
            throw new RuntimeException(
                "Sseclient ID registration failure on ssemanager" + e.getMessage());
        }
    }

    /**
     * Snapshot is used every 5 seconds to send real -time progress.
     *
     * @param snapshot the result of a single scenario step
     */
    @Override
    public void publish(LoadTestSnapshot snapshot) {
        try {
            String snapshotJson = objectMapper.writeValueAsString(snapshot);
            sseManager.broadcastEvent("snapshot", snapshotJson);
        } catch (IOException e) {
            throw new RuntimeException(
                "Error occurs during the load test execution" + e.getMessage());
        }
    }

    /**
     * Test summary is broadcast to all connected SSE clients to complete the load test. Converted
     * Loadtestsummary object to json string and send it by using it. Ssemonager is managed.
     *
     * @param summary the summary of the completed load test including details such as test name,
     *                description, start and end times, results, and endpoints
     * @throws RuntimeException
     */
    @Override
    public void complete(LoadTestSummary summary) {
        try {
            String summaryJson = objectMapper.writeValueAsString(summary);
            sseManager.broadcastEvent("summary", summaryJson);
        } catch (IOException e) {
            throw new RuntimeException(
                "Error occurs during the transmission of the load test result" + e.getMessage());
        }
    }
}
