package com.apighost.web.sse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SSEManager is responsible for managing Server-Sent Event (SSE) client connections
 * and broadcasting events or data to clients. This class implements a singleton
 * pattern, ensuring only one instance is created and shared across the application.
 *
 *  @author sung-jun98
 *  @version BETA-0.0.1
 */
public class SSEManager {

    private static SSEManager instance;
    private final Map<String, SSEClient> clients = new ConcurrentHashMap<>();

    public static synchronized SSEManager getInstance() {
        if (instance == null) {
            instance = new SSEManager();
        }
        return instance;
    }

    private SSEManager() {}

    /**
     * Registers a new SSE client using the provided client ID.
     *
     * @param clientId the unique identifier of the client to register
     * @param client the SSEClient instance representing the client connection
     */
    public void registerClient(String clientId, SSEClient client) {
        clients.put(clientId, client);
    }

    /**
     * Retrieves an SSEClient associated with the specified client ID.
     *
     * @param clientId the unique identifier of the client whose SSEClient instance is to be retrieved
     * @return the SSEClient instance associated with the given client ID, or null if no client is found
     */
    public SSEClient getClient(String clientId) {
        return clients.get(clientId);
    }

    /**
     * Removes the SSEClient associated with the specified client ID.
     *
     * @param clientId the unique identifier of the client to be removed
     * @return the SSEClient instance that was removed, or null if no client was associated with the specified ID
     */
    public SSEClient removeClient(String clientId) {
        return clients.remove(clientId);
    }

    /**
     * Broadcasts an event along with data to all registered SSE clients.
     *
     * @param event the name of the event to broadcast
     * @param data the data to send along with the event
     * @throws IOException
     */
    public void broadcastEvent(String event, String data) throws IOException {
        for (SSEClient client : clients.values()) {
            client.sendEvent(event, data);
        }
    }

    /**
     * Sends a server-sent event to a specific client identified by the client ID.
     *
     * @param clientId
     * @param event the name of the event to send
     * @param data the data to send along with the event
     * @throws IOException
     */
    public void sendEvent(String clientId, String event, String data) throws IOException {
        SSEClient client = clients.get(clientId);
        if (client != null) {
            client.sendEvent(event, data);
        }
    }

    /**
     * Retrieves the number of currently registered SSE clients being managed.
     *
     * @return the total count of registered clients
     */
    public int getClientCount() {
        return clients.size();
    }
}
