package com.apighost.model.loadtest.result;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Each endpoint has a Result object, which is the corresponding Loadtest indicator.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = Endpoint.Builder.class)
public class Endpoint {

    private final String url;
    private final EndpointResult result;

    private Endpoint(Builder builder) {
        this.url = builder.url;
        this.result = builder.result;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private String url;
        private EndpointResult result;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder result(EndpointResult result) {
            this.result = result;
            return this;
        }

        public Endpoint build() {
            return new Endpoint(this);
        }
    }

    /**
     * Getter
     */
    public String getUrl() {
        return url;
    }

    public EndpointResult getResult() {
        return result;
    }
}
