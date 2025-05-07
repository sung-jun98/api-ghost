package com.apighost.model.scenario;

import com.apighost.model.scenario.result.ResultStep;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;

/**
 * Represents the overall result of a scenario execution.
 *
 * @author haazz
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = ScenarioResult.Builder.class)
public class ScenarioResult {

    private final String name;
    private final String description;
    private final String executedAt;
    private final long totalDurationMs;
    private final long averageDurationMs;
    private final String filePath;
    private final String baseUrl;
    private final boolean isScenarioSuccess;
    private final List<ResultStep> results;

    private ScenarioResult(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.executedAt = builder.executedAt;
        this.totalDurationMs = builder.totalDurationMs;
        this.averageDurationMs = builder.averageDurationMs;
        this.filePath = builder.filePath;
        this.baseUrl = builder.baseUrl;
        this.isScenarioSuccess = builder.isScenarioSuccess;
        this.results = builder.results;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private String name;
        private String description;
        private String executedAt;
        private long totalDurationMs;
        private long averageDurationMs;
        private String filePath;
        private String baseUrl;
        private boolean isScenarioSuccess;
        private List<ResultStep> results;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder executedAt(String executedAt) {
            this.executedAt = executedAt;
            return this;
        }

        public Builder totalDurationMs(long totalDurationMs) {
            this.totalDurationMs = totalDurationMs;
            return this;
        }

        public Builder averageDurationMs(long averageDurationMs) {
            this.averageDurationMs = averageDurationMs;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder isScenarioSuccess(boolean isScenarioSuccess) {
            this.isScenarioSuccess = isScenarioSuccess;
            return this;
        }

        public Builder results(List<ResultStep> results) {
            this.results = results;
            return this;
        }

        public ScenarioResult build() {
            return new ScenarioResult(this);
        }
    }

    /**
     * Getter
     */
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getExecutedAt() {
        return executedAt;
    }

    public long getTotalDurationMs() {
        return totalDurationMs;
    }

    public long getAverageDurationMs() {
        return averageDurationMs;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public boolean getIsScenarioSuccess() {
        return isScenarioSuccess;
    }

    public List<ResultStep> getResults() {
        return results;
    }
}

