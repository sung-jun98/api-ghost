package com.apighost.model.loadtest.result;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 *
 *
 *  @author sung-jun98
 *  @version BETA-0.0.1
 */
@JsonDeserialize(builder = EndpointResult.Builder.class)
public class EndpointResult {

    private final String endpoint;
    private final Results results;

    private EndpointResult(Builder builder) {
        this.endpoint = builder.endpoint;
        this.results = builder.results;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private String endpoint;
        private Results results;

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder results(Results results) {
            this.results = results;
            return this;
        }

        public EndpointResult build() {
            return new EndpointResult(this);
        }
    }

    /** getters */
    public String getEndpoint() {
        return endpoint;
    }

    public Results getResults() {
        return results;
    }
}
