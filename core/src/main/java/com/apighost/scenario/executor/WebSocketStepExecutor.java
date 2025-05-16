package com.apighost.scenario.executor;

import com.apighost.model.scenario.WebSocketStatusCode;
import com.apighost.model.scenario.request.Request;
import com.apighost.model.scenario.result.ResultStep;
import com.apighost.model.scenario.step.Step;
import com.apighost.util.ExecutionTimer;
import com.apighost.util.TimeUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static com.apighost.scenario.executor.WebSocketManager.*;

/**
 * Executes a scenario step that uses the WebSocket protocol. Supports STOMP over WebSocket commands
 * including CONNECT, SEND, SUBSCRIBE, UNSUBSCRIBE, and CLOSE.
 *
 * <p>Each thread maintains its own WebSocket session and subscription ID mappings,
 * which are managed in {@link WebSocketManager}. The listener responds to connection
 * acknowledgments and completes handshake logic.
 *
 * @author kobenlys
 * @version BETA-0.0.1
 */
public class WebSocketStepExecutor implements StepExecutor {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Executes a WebSocket-related step using the provided {@link Step} configuration.
     *
     * @param stepKey   the unique key of the current step
     * @param step      the step to be executed
     * @param store     shared memory across steps for context transfer
     * @param timeoutMs the timeout duration in milliseconds for blocking operations
     * @return a {@link ResultStep} containing execution results
     * @throws IOException              if an I/O error occurs
     * @throws InterruptedException     if the operation is interrupted
     * @throws IllegalArgumentException if required step fields are null or invalid
     */
    @Override
    public ResultStep execute(String stepKey, Step step, Map<String, Object> store, long timeoutMs)
        throws IOException, InterruptedException {

        if (step == null || (stepKey == null || stepKey.isEmpty())) {
            throw new IllegalArgumentException("Step Information must not be null");
        }
        if (step.getRequest() == null) {
            throw new IllegalArgumentException("Request must not be null. : " + stepKey);
        }

        Request request = step.getRequest();
        boolean isRequestSuccess = true;

        ExecutionTimer.DurationResult<WebSocketStatusCode> summary = ExecutionTimer.execute(
            () -> switch (request.getMethod()) {
                case CONNECT -> connect(request.getUrl(), timeoutMs);
                case SEND -> send(request.getUrl(), request.getBody().getJson());
                case SUBSCRIBE -> subscribe(request);
                case UNSUBSCRIBE -> unSubscribe(request);
                case DISCONNECT -> disconnect(timeoutMs);
                default -> throw new UnsupportedOperationException(
                    "Unknown method: " + request.getMethod());
            });

        String nextStep =
            step.getRoute().stream().findFirst().map(route -> route.getThen().getStep()).orElse("");

        if (EnumSet.of(WebSocketStatusCode.CONNECT_FAILED, WebSocketStatusCode.CLOSE_FAILED,
                WebSocketStatusCode.SEND_FAILED, WebSocketStatusCode.SUBSCRIBE_FAILED,
                WebSocketStatusCode.UNSUBSCRIBE_FAILED, WebSocketStatusCode.NULL_SESSION)
            .contains(summary.result)) {
            nextStep = null;
            isRequestSuccess = false;
        }

        return new ResultStep.Builder().stepName(stepKey).type(step.getType())
            .method(request.getMethod()).url(request.getUrl()).requestHeader(request.getHeader())
            .requestBody(request.getBody()).responseHeaders(Map.of()).responseBody("{}")
            .status(summary.result.getStatusCode()).startTime(String.valueOf(summary.startTime))
            .endTime(TimeUtils.convertFormat(summary.startTime)).durationMs(summary.durationTime)
            .isRequestSuccess(isRequestSuccess).nextStep(nextStep).route(step.getRoute()).build();
    }

    /**
     * Initiates a WebSocket connection and sends a STOMP CONNECT frame. Awaits connection
     * acknowledgment within the specified timeout.
     *
     * @param url       the WebSocket server URL
     * @param timeoutMs the timeout in milliseconds to wait for connection acknowledgment
     * @return {@link WebSocketStatusCode} indicating the result of the operation
     */
    private WebSocketStatusCode connect(String url, long timeoutMs) {

        try {

            CompletableFuture<Boolean> connectAck = new CompletableFuture<>();
            putConnectAck(connectAck);
            WebSocket connectWebSocket = HttpClient.newHttpClient().newWebSocketBuilder()
                .buildAsync(URI.create(url), new BasicListener(Thread.currentThread().getId()))
                .join();

            putWebSocket(connectWebSocket);
            connectWebSocket.sendText(buildStompConnectFrame(), true);

            boolean connected = connectedAckResponse(timeoutMs, TimeUnit.MILLISECONDS);

            return connected ?
                WebSocketStatusCode.CONNECT_SUCCESS :
                WebSocketStatusCode.CONNECT_FAILED;
        } catch (NullPointerException e) {

            return WebSocketStatusCode.NULL_SESSION;
        } catch (Exception e) {

            return WebSocketStatusCode.CONNECT_FAILED;
        } finally {
            removeConnectAck();
        }
    }

    /**
     * Sends a STOMP SUBSCRIBE frame to the given destination.
     *
     * @param request the request containing the subscription destination
     * @return {@link WebSocketStatusCode} indicating success or failure
     */
    private WebSocketStatusCode subscribe(Request request) {

        try {

            String subscribeId = UUID.randomUUID().toString();
            String subscribeFrame = buildStompSubscribeFrame(request.getUrl(), subscribeId);
            getWebSocket().sendText(subscribeFrame, true);
            saveSubScribeId(request.getUrl(), subscribeId);

            return WebSocketStatusCode.SUBSCRIBE_SUCCESS;
        } catch (NullPointerException e) {
            return WebSocketStatusCode.NULL_SESSION;
        } catch (Exception e) {
            return WebSocketStatusCode.SUBSCRIBE_FAILED;
        }
    }

    /**
     * Sends a STOMP UNSUBSCRIBE frame to cancel a previous subscription.
     *
     * @param request the request containing the subscription destination
     * @return {@link WebSocketStatusCode} indicating success or failure
     */
    private WebSocketStatusCode unSubscribe(Request request) {

        try {

            String subscribeId = getSubscribeAndRemove(request.getUrl());
            if (subscribeId == null || subscribeId.isEmpty()) {
                return WebSocketStatusCode.UNSUBSCRIBE_FAILED;
            }

            String frame = buildStompUnsubscribeFrame(subscribeId);
            getWebSocket().sendText(frame, true);

            return WebSocketStatusCode.UNSUBSCRIBE_SUCCESS;
        } catch (NullPointerException e) {
            return WebSocketStatusCode.NULL_SESSION;
        } catch (Exception e) {
            return WebSocketStatusCode.UNSUBSCRIBE_FAILED;
        }
    }

    /**
     * Sends a STOMP SEND frame to a specific topic with the given payload.
     *
     * @param topic   the STOMP destination topic
     * @param payload the data to be serialized and sent
     * @return {@link WebSocketStatusCode} indicating success or failure
     */
    private WebSocketStatusCode send(String topic, Object payload) {

        try {

            String sendFrame = buildStompSendFrame(topic, payload);
            getWebSocket().sendText(sendFrame, true);

            return WebSocketStatusCode.SEND_SUCCESS;
        } catch (NullPointerException e) {
            return WebSocketStatusCode.NULL_SESSION;
        } catch (Exception e) {
            return WebSocketStatusCode.SEND_FAILED;
        }
    }

    /**
     * Closes the WebSocket session by sending a CLOSE frame.
     *
     * @param timeoutMs the timeout for waiting on the close acknowledgment
     * @return {@link WebSocketStatusCode} indicating the result of the operation
     */
    private WebSocketStatusCode disconnect(long timeoutMs) {

        try {

            getWebSocket().sendClose(WebSocket.NORMAL_CLOSURE, "Close from ApiGhost")
                .orTimeout(timeoutMs, TimeUnit.MILLISECONDS).join();
            removeWebSocket();

            return WebSocketStatusCode.CLOSE_SUCCESS;
        } catch (NullPointerException e) {
            return WebSocketStatusCode.NULL_SESSION;
        } catch (Exception e) {
            return WebSocketStatusCode.CLOSE_FAILED;
        }
    }

    /**
     * Clears all WebSocket-related resources from the current thread context.
     */
    private static void clearAll() {
        WebSocketManager.removeAll();
    }

    /**
     * Basic WebSocket listener implementation that handles CONNECTED frame acknowledgment.
     */
    private static class BasicListener implements WebSocket.Listener {

        private final long originalThreadId;

        public BasicListener(long originalThreadId) {
            this.originalThreadId = originalThreadId;
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {

            if (data.toString().startsWith("CONNECTED")) {
                CompletableFuture<Boolean> future = getConnectAck(originalThreadId);
                if (future != null) {
                    future.complete(true);
                }
            }
            webSocket.request(1);
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            return CompletableFuture.completedFuture(null);
        }
    }

    /**
     * Stomp Message Builder
     **/
    private static String buildStompConnectFrame() {
        return "CONNECT\n" +
            "accept-version:1.2\n" +
            "host:localhost\n" +
            "heart-beat:10000,10000\n\n" +
            "\u0000";
    }

    private static String buildStompSendFrame(String topic, Object payload)
        throws JsonProcessingException {
        String jsonObject = objectMapper.writeValueAsString(payload);
        return "SEND\ndestination:" + topic + "\ncontent-type:text/plain\n\n" + jsonObject
            + "\u0000";
    }

    private static String buildStompSubscribeFrame(String topic, String id) {
        return "SUBSCRIBE\nid:" + id + "\ndestination:" + topic + "\n\n\u0000";
    }

    private static String buildStompUnsubscribeFrame(String id) {
        return "UNSUBSCRIBE\nid:" + id + "\n\n\u0000";
    }
}
