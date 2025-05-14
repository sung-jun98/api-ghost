package com.apighost.model.loadtest.result;

import com.apighost.model.collector.Endpoint;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * SSE sends the test execution progress to the GUI periodically.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = LoadTestSummary.Builder.class)
public class LoadTestSummary {

    private final String name;
    private final String description;
    @JsonProperty("startTime")
    private final String startTime;

    @JsonProperty("endTime")
    private final String endTime;

    private final Result result;

    private final List<Endpoint> endpoints;

    private LoadTestSummary(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.result = builder.result;
        this.endpoints = builder.endpoints;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private String name;
        private String description;
        private String startTime;
        private String endTime;
        private Result result;
        private List<Endpoint> endpoints;

        @JsonProperty("name")
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        @JsonProperty("description")
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        @JsonProperty("startTime")
        public Builder startTime(String startTime) {
            this.startTime = startTime;
            return this;
        }

        @JsonProperty("endTime")
        public Builder endTime(String endTime) {
            this.endTime = endTime;
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

        public LoadTestSummary build() {
            return new LoadTestSummary(this);
        }
    }

    /**
     * Getter
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("startTime")
    public String getStartTime() {
        return startTime;
    }

    @JsonProperty("endTime")
    public String getEndTime() {
        return endTime;
    }

    public Result getResult() {
        return result;
    }

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }
}
