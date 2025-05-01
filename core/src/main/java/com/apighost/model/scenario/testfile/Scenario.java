package com.apighost.model.scenario.testfile;

import java.util.Map;

/**
 * Metabolic definition of scenarios
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class Scenario {

    private String name;
    private String description;
    private String scenarioId;
    private Integer timeoutMs;

    private Map<String, Object> variables;
    private Map<String, Step> steps;


    private Scenario(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.scenarioId = builder.scenarioId;
        this.timeoutMs = builder.timeoutMs;
        this.variables = builder.variables;
        this.steps = builder.steps;
    }

    /**
     * Builder
     */
    public static class Builder {

        private String name;
        private String description;
        private String scenarioId;
        private Integer timeoutMs;
        private Map<String, Object> variables;
        private Map<String, Step> steps;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder scenarioId(String scenarioId) {
            this.scenarioId = scenarioId;
            return this;
        }

        public Builder timeoutMs(Integer timeoutMs) {
            this.timeoutMs = timeoutMs;
            return this;
        }

        public Builder variables(Map<String, Object> variables) {
            this.variables = variables;
            return this;
        }

        public Builder steps(Map<String, Step> steps) {
            this.steps = steps;
            return this;
        }

        public Scenario build() {
            return new Scenario(this);
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

    public String getScenarioId() {
        return scenarioId;
    }

    public Integer getTimeoutMs() {
        return timeoutMs;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public Map<String, Step> getSteps() {
        return steps;
    }
}
