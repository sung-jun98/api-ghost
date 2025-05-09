package com.apighost.web.controller;

import com.apighost.scenario.callback.ScenarioResultCallback;
import com.apighost.scenario.executor.ScenarioTestExecutor;
import com.apighost.web.util.FileType;
import com.apighost.web.util.FileUtil;
import com.apighost.web.util.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


import com.apighost.model.scenario.Scenario;
import com.apighost.parser.scenario.reader.ScenarioReader;
import com.apighost.parser.scenario.reader.YamlScenarioReader;
import com.apighost.web.sse.SSEClient;
import com.apighost.web.sse.SSEManager;
import com.apighost.web.sse.ScenarioTestSSECallback;

import jakarta.servlet.AsyncContext;
import java.io.File;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Servlet for handling scenario test execution and sending results via SSE
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class ScenarioTestController implements ApiController {
    private static final int ASYNC_TIMEOUT_MS = 300000;

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final SSEManager sseManager = SSEManager.getInstance();
    private static Scenario scenario;

    /**
     * Handles HTTP GET requests to initiate a scenario test based on the requested scenario name.
     * Supports Server-Sent Events (SSE) for real-time communication with the client.
     *
     * @param request  the HttpServletRequest object that contains the client request
     * @param response the HttpServletResponse object that contains the response to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** Confirmation of asynchronous support */
        if (!request.isAsyncSupported()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonUtils.writeErrorResponse(response, "Async not supported",
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        /** Get scenario name parameter */
        String scenarioName = request.getParameter("scenarioName");
        if (scenarioName == null || scenarioName.isEmpty()) {
            JsonUtils.writeErrorResponse(response, "Missing required parameter: scenarioName",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        /** Set up async context
         * 5-minute timeout */
        AsyncContext asyncContext = request.startAsync();
        asyncContext.setTimeout(ASYNC_TIMEOUT_MS);

        /** Generate client ID and register SSE client */
        String clientId = UUID.randomUUID().toString();
        SSEClient sseClient = new SSEClient(clientId, response);
        sseManager.registerClient(clientId, sseClient);

        /** Scenario test asynchronous execution */
        executorService.submit(() -> {
            try {

                /** 1. FileUtil -> file Search */
                File targetDir = FileUtil.findDirectory(FileType.SCENARIO);

                /** Refraction to be considered for other types of test files*/
                File[] files = targetDir.listFiles((dir, name) ->
                                                       name.toLowerCase().endsWith(".yaml") ||
                                                           name.toLowerCase().endsWith(".yml")
                );

                /** 2. Read the scenario file */
                for (File file : files) {
                    String searchedName = file.getName();

                    if (searchedName.equals(scenarioName)) {
                        ScenarioReader reader = new YamlScenarioReader();
                        scenario = reader.readScenario(file.getAbsolutePath());
                    }
                }

                if (scenario == null) {
                    sseClient.sendEvent("error",
                        "Error executing scenario test: ");
                    closeConnection(clientId);
                }

                /** 3. Scenario test executor creation and callback setting */
                ScenarioTestExecutor executor = new ScenarioTestExecutor();
                ScenarioResultCallback callback = new ScenarioTestSSECallback(clientId,
                    sseManager);

                /** 4. Callback registration at the same time as the scenario test is executed
                 Connection End Logic is located in the onscenariocompleted of the callback function ScenariotestSecallback. */
                executor.execute(scenario, callback);

            } catch (Exception e) {
                try {
                    sseClient.sendEvent("error",
                        "Error executing scenario test: " + e.getMessage());
                    closeConnection(clientId);
                } catch (IOException ex) {
                    System.err.println("Error sending error event: " + ex.getMessage());
                    ex.printStackTrace();
                } finally {
                    asyncContext.complete();
                }
            }
        });
    }

    private void closeConnection(String clientId) {
        try {
            SSEClient client = sseManager.removeClient(clientId);
            if (client != null) {
                client.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing SSE connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
