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
    private final Result result;
    private final List<Endpoint> endpoints;

    private LoadTestSnapshot(Builder builder) {
        this.timeStamp = builder.timeStamp;
        this.result = builder.result;
        this.endpoints = builder.endpoints;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private String timeStamp;
        private Result result;
        private List<Endpoint> endpoints;

        public Builder timeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public Builder result(Result result) {
            this.result = result;
            return this;
        }

        public Builder endpoints(List<Endpoint> endpoints) {
            this.endpoints = endpoints;
            return this;
        }

        public LoadTestSnapshot build() {
            return new LoadTestSnapshot(this);
        }
    }

    /**
     * Getter
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    public Result getResult() {
        return result;
    }

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }
}
