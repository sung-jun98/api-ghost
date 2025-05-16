package com.apighost.model.loadtest.result;

import com.apighost.model.loadtest.result.metric.HttpReqDuration;
import com.apighost.model.loadtest.result.metric.HttpReqFailed;
import com.apighost.model.loadtest.result.metric.HttpReqs;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classes containing practical load test endpoint results
 *
 * @author haazz
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = EndpointResult.Builder.class)
public class EndpointResult {

    @JsonProperty("http_reqs")
    private final HttpReqs httpReqs;

    @JsonProperty("http_req_duration")
    private final HttpReqDuration httpReqDuration;

    @JsonProperty("http_req_failed")
    private final HttpReqFailed httpReqFailed;

    private final int vus;

    private EndpointResult(Builder builder) {
        this.httpReqs = builder.httpReqs;
        this.httpReqDuration = builder.httpReqDuration;
        this.httpReqFailed = builder.httpReqFailed;
        this.vus = builder.vus;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private HttpReqs httpReqs;
        private HttpReqDuration httpReqDuration;
        private HttpReqFailed httpReqFailed;
        private int vus;

        @JsonProperty("http_reqs")
        public Builder httpReqs(HttpReqs httpReqs) {
            this.httpReqs = httpReqs;
            return this;
        }

        @JsonProperty("http_req_duration")
        public Builder httpReqDuration(HttpReqDuration httpReqDuration) {
            this.httpReqDuration = httpReqDuration;
            return this;
        }

        @JsonProperty("http_req_failed")
        public Builder httpReqFailed(HttpReqFailed httpReqFailed) {
            this.httpReqFailed = httpReqFailed;
            return this;
        }

        public Builder vus(int vus) {
            this.vus = vus;
            return this;
        }

        public EndpointResult build() {
            return new EndpointResult(this);
        }
    }

    /**
     * Getter
     */
    @JsonProperty("http_reqs")
    public HttpReqs getHttpReqs() {
        return httpReqs;
    }

    @JsonProperty("http_req_duration")
    public HttpReqDuration getHttpReqDuration() {
        return httpReqDuration;
    }

    @JsonProperty("http_req_failed")
    public HttpReqFailed getHttpReqFailed() {
        return httpReqFailed;
    }

    public int getVus() {
        return vus;
    }
}
