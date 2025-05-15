package com.apighost.scenario.executor;

import java.net.http.WebSocket;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Manages WebSocket connections, subscriptions, and connection acknowledgments per thread.
 * <p>
 * This class provides thread-local storage and utilities for:
 * <ul>
 *   <li>Tracking individual WebSocket sessions</li>
 *   <li>Managing topic subscriptions per thread</li>
 *   <li>Handling connection acknowledgment using {@link CompletableFuture}</li>
 * </ul>
 *
 * @author kobenlys
 * @version BETA-0.0.1
 */
public class WebSocketManager {

    /** Stores WebSocket sessions per thread */
    private static final ConcurrentHashMap<Long, WebSocket> webSocketStore =
        new ConcurrentHashMap<>();

    /** Stores topic-to-subscription ID mappings per thread */
    private static final ConcurrentHashMap<Long, ConcurrentHashMap<String, Set<String>>>
        subscribeStore = new ConcurrentHashMap<>();

    /** Stores connection acknowledgment futures per thread */
    private static final ConcurrentHashMap<Long, CompletableFuture<Boolean>> connectAckStore =
        new ConcurrentHashMap<>();

    /**
     * Associates the current thread's ID with the given WebSocket instance.
     *
     * @param webSocket the WebSocket instance to store
     */
    public static void putWebSocket(WebSocket webSocket) {
        webSocketStore.put(getCallerThreadId(), webSocket);
    }

    /**
     * Retrieves the WebSocket associated with the current thread.
     *
     * @return the WebSocket for the current thread, or null if not set
     */
    public static WebSocket getWebSocket() {
        return webSocketStore.get(getCallerThreadId());
    }

    /**
     * Removes the WebSocket associated with the current thread.
     */
    public static void removeWebSocket() {
        webSocketStore.remove(getCallerThreadId());
    }

    /**
     * Retrieves and removes a single subscription ID for the given topic.
     *
     * @param topic the topic to unsubscribe from
     * @return a subscription ID if available; otherwise, null
     */
    public static String getSubscribeAndRemove(String topic){
        Set<String> subscriptions = subscribeStore
            .getOrDefault(getCallerThreadId(), new ConcurrentHashMap<>())
            .get(topic);

        if (subscriptions != null && !subscriptions.isEmpty()) {
            Iterator<String> iterator = subscriptions.iterator();
            if (iterator.hasNext()) {
                String removed = iterator.next();
                iterator.remove();
                return removed;
            }
        }
        return null;
    }

    /**
     * Saves a subscription ID for the specified topic under the current thread.
     *
     * @param topic the topic being subscribed to
     * @param subscribeId the subscription ID to store
     */
    public static void saveSubScribeId(String topic, String subscribeId){
        subscribeStore.computeIfAbsent(getCallerThreadId(), K -> new ConcurrentHashMap<>())
            .computeIfAbsent(topic, t -> Collections.newSetFromMap(new ConcurrentHashMap<>()))
            .add(subscribeId);
    }

    /**
     * Retrieves the connection acknowledgment future for the specified thread.
     *
     * @param threadId the thread ID
     * @return the corresponding CompletableFuture, or null if not present
     */
    public static CompletableFuture<Boolean> getConnectAck(long threadId){
        return connectAckStore.get(threadId);
    }

    /**
     * Stores a new connection acknowledgment future for the current thread.
     *
     * @param connectAck the CompletableFuture to store
     */
    public static void putConnectAck(CompletableFuture<Boolean> connectAck){
        connectAckStore.put(getCallerThreadId(), connectAck);
    }

    /**
     * Removes the connection acknowledgment future for the current thread.
     */
    public static void removeConnectAck(){
        connectAckStore.remove(getCallerThreadId());
    }

    /**
     * Waits for a connection acknowledgment response within the specified timeout.
     *
     * @param timeoutMs the timeout duration
     * @param timeUnit the unit of time
     * @return true if acknowledgment was received; false otherwise
     */
    public static boolean connectedAckResponse(long timeoutMs, TimeUnit timeUnit){
        try{
            return connectAckStore.get(getCallerThreadId()).get(timeoutMs, timeUnit);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Clears all stored WebSocket sessions, subscriptions, and acknowledgment futures.
     * <p>
     * Sends a close frame to all active WebSocket sessions before clearing.
     */
    public static void removeAll() {
        webSocketStore.values().forEach(ws -> ws.sendClose(WebSocket.NORMAL_CLOSURE, "Clear"));
        connectAckStore.clear();
        subscribeStore.clear();
        webSocketStore.clear();
    }

    /**
     * Retrieves the ID of the current thread.
     *
     * @return the current thread ID
     */
    private static long getCallerThreadId(){
        return Thread.currentThread().getId();
    }
}
