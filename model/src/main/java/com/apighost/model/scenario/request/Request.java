package com.apighost.model.scenario.request;

import com.apighost.model.scenario.step.HTTPMethod;
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

    private HTTPMethod method;
    private String url;
    private Map<String, String> header;
    private RequestBody body;

    private Request(Builder builder) {
        this.method = builder.method;
        this.url = builder.url;
        this.header = builder.header;
        this.body = builder.body;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private HTTPMethod method;
        private String url;
        private Map<String, String> header;
        private RequestBody body;

        public Builder method(HTTPMethod method) {
            this.method = method;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder header(Map<String, String> header) {
            this.header = header;
            return this;
        }

        public Builder body(RequestBody body) {
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
    public HTTPMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public RequestBody getBody() {
        return body;
    }
}
