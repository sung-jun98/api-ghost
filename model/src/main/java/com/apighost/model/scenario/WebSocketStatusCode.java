package com.apighost.model.scenario;

public enum WebSocketStatusCode {
    CONNECT_SUCCESS(101),
    CONNECT_FAILED(1006),
    SUBSCRIBE_SUCCESS(0),
    SUBSCRIBE_FAILED(500),
    UNSUBSCRIBE_SUCCESS(0),
    UNSUBSCRIBE_FAILED(500),
    SEND_SUCCESS(0),
    SEND_FAILED(500),
    CLOSE_SUCCESS(100),
    CLOSE_FAILED(500),
    NULL_SESSION(404);

    private final int statusCode;

    WebSocketStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
