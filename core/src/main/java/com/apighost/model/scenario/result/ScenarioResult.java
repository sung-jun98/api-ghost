package com.apighost.model.scenario.result;

import java.util.List;

/**
 * Represents the overall result of a scenario execution.
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class ScenarioResult {
    private final String scenarioId;
    private final String name;
    private final String description;
    private final String executedAt;
    private final int totalDurationMs;
    private final int averageDurationMs;
    private final String filePath;
    private final String baseUrl;
    private final boolean isScenarioSuccess;
    private final List<StepResult> results;

    private ScenarioResult(Builder builder) {
        this.scenarioId = builder.scenarioId;
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
    public static class Builder {
        private String scenarioId;
        private String name;
        private String description;
        private String executedAt;
        private int totalDurationMs;
        private int averageDurationMs;
        private String filePath;
        private String baseUrl;
        private boolean isScenarioSuccess;
        private List<StepResult> results;

        public Builder scenarioId(String scenarioId) {
            this.scenarioId = scenarioId;
            return this;
        }

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

        public Builder totalDurationMs(int totalDurationMs) {
            this.totalDurationMs = totalDurationMs;
            return this;
        }

        public Builder averageDurationMs(int averageDurationMs) {
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

        public Builder scenarioSuccess(boolean isScenarioSuccess) {
            this.isScenarioSuccess = isScenarioSuccess;
            return this;
        }

        public Builder results(List<StepResult> results) {
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
    public String getScenarioId() {
        return scenarioId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getExecutedAt() {
        return executedAt;
    }

    public int getTotalDurationMs() {
        return totalDurationMs;
    }

    public int getAverageDurationMs() {
        return averageDurationMs;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public boolean isScenarioSuccess() {
        return isScenarioSuccess;
    }

    public List<StepResult> getResults() {
        return results;
    }
}

