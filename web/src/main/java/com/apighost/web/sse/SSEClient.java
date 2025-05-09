package com.apighost.web.sse;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletResponse;

/**
 * SSEClient is a utility class that manages a single Server-Sent Events (SSE) connection with a
 * client.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class SSEClient {

    private final String clientId;
    private final HttpServletResponse response;
    private final PrintWriter writer;
    private boolean isConnected = true;

    public SSEClient(String clientId, HttpServletResponse response) throws IOException {
        this.clientId = clientId;
        this.response = response;

        this.response.setContentType("text/event-stream");
        this.response.setCharacterEncoding("UTF-8");
        this.response.setHeader("Cache-Control", "no-cache");
        this.response.setHeader("Connection", "keep-alive");
        this.writer = this.response.getWriter();

        sendMessage("Connection successfully established.");
    }

    /**
     * Returns the client ID
     *
     * @return client Id
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Sends a message
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        if (isConnected) {
            writer.write("data: " + message + "\n\n");
            writer.flush();
        }
    }

    /**
     * Sends data with an event
     *
     * @param event Event name
     * @param data  data to send
     * @throws IOException
     */
    public void sendEvent(String event, String data) throws IOException {
        if (isConnected) {
            writer.write("event: " + event + "\n");
            writer.write("data: " + data + "\n\n");
            writer.flush();
        }
    }

    /**
     * Closes the connection
     *
     * @throws IOException
     */
    public void close() throws IOException {
        if (isConnected) {
            isConnected = false;
            writer.close();
        }
    }

    /**
     * Returns the connection status
     *
     * @return CONNECTION
     */
    public boolean isConnected() {
        return isConnected;
    }
}
