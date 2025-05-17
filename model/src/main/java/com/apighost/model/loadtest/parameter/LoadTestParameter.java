package com.apighost.model.loadtest.parameter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;

/**
 * Settings parametes required for load test It is made of YAML file and will be between DTO and
 * YAML through parser.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = LoadTestParameter.Builder.class)
public class LoadTestParameter {

    private final String name;
    private final String description;
    private final long thinkTimeMs;
    @JsonProperty("stage")
    private final List<Stage> stages;
    private final List<String> scenarios;

    private LoadTestParameter(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.thinkTimeMs = builder.thinkTimeMs;
        this.stages = builder.stages;
        this.scenarios = builder.scenarios;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private String name;
        private String description;
        private long thinkTimeMs;
        private List<Stage> stages;
        private List<String> scenarios;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder thinkTimeMs(long thinkTimeMs) {
            this.thinkTimeMs = thinkTimeMs;
            return this;
        }

        @JsonProperty("stage")
        public Builder stages(List<Stage> stages) {
            this.stages = stages;
            return this;
        }

        public Builder scenarios(List<String> scenarios) {
            this.scenarios = scenarios;
            return this;
        }

        public LoadTestParameter build() {
            return new LoadTestParameter(this);
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

    public long getThinkTimeMs() {
        return thinkTimeMs;
    }

    @JsonProperty("stage")
    public List<Stage> getStages() {
        return stages;
    }

    public List<String> getScenarios() {
        return scenarios;
    }
}
