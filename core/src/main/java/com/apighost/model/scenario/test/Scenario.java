package com.apighost.model.scenario.test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.Map;

/**
 * Metabolic definition of scenarios
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = Scenario.Builder.class)
public class Scenario {

    private String name;
    private String description;
    private String scenarioId;
    private Integer timeoutMs;

    private Map<String, Object> store;
    private Map<String, Step> steps;


    private Scenario(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.scenarioId = builder.scenarioId;
        this.timeoutMs = builder.timeoutMs;
        this.store = builder.store;
        this.steps = builder.steps;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private String name;
        private String description;
        private String scenarioId;
        private Integer timeoutMs;
        private Map<String, Object> store;
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

        public Builder store(Map<String, Object> store) {
            this.store = store;
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

    public Map<String, Object> getStore() {
        return store;
    }

    public Map<String, Step> getSteps() {
        return steps;
    }
}
