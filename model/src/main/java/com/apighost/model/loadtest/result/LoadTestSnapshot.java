package com.apighost.model.loadtest.result;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;

/**
 * Model used to send test information periodically with SSE
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = LoadTestSnapshot.Builder.class)
public class LoadTestSnapshot {

    private final String timeStamp;
    private final String baseUrl;
    private final Results results;
    private final List<EndpointResult> endpoints;

    private LoadTestSnapshot(Builder builder) {
        this.timeStamp = builder.timeStamp;
        this.baseUrl = builder.baseUrl;
        this.results = builder.results;
        this.endpoints = builder.endpoints;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private String timeStamp;
        private String baseUrl;
        private Results results;
        private List<EndpointResult> endpoints;

        public Builder timeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder results(Results results) {
            this.results = results;
            return this;
        }

        public Builder endpoints(List<EndpointResult> endpoints) {
            this.endpoints = endpoints;
            return this;
        }

        public LoadTestSnapshot build() {
            return new LoadTestSnapshot(this);
        }
    }

    /** getters */
    public String getTimeStamp() {
        return timeStamp;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Results getResults() {
        return results;
    }

    public List<EndpointResult> getEndpoints() {
        return endpoints;
    }
}
