package com.apighost.model.scenario.testfile;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.Map;

/**
 * Definition of requests to be sent to that endpoint
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = Request.Builder.class)
public class Request {

    private String method;
    private String url;
    private Map<String, String> headers;
    private Map<String, Map<String, Object>> body;

    private Request(Builder builder) {
        this.method = builder.method;
        this.url = builder.url;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private String method;
        private String url;
        private Map<String, String> headers;
        private Map<String, Map<String, Object>> body;

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder body(Map<String, Map<String, Object>> body) {
            this.body = body;
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }

    /**
     * Getter
     */
    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, Map<String, Object>> getBody() {
        return body;
    }
}
