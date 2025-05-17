package com.apighost.model.loadtest.parameter;

import com.apighost.model.scenario.Scenario;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;

/**
 * LoadtestParameter is a model corresponding to the YAML file, and that loadTestExecuteparameter is
 * a model to run the load test. The difference is that Scerios is a Scenario object, not a string,
 * unlike LoadtestParameter.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = LoadTestExecuteParameter.Builder.class)
public class LoadTestExecuteParameter {

    private final String name;
    private final String description;
    private final long thinkTimeMs;
    @JsonProperty("stage")
    private final List<Stage> stages;
    private final List<Scenario> scenarios;

    private LoadTestExecuteParameter(LoadTestExecuteParameter.Builder builder) {
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
        private List<Scenario> scenarios;

        public LoadTestExecuteParameter.Builder name(String name) {
            this.name = name;
            return this;
        }

        public LoadTestExecuteParameter.Builder description(String description) {
            this.description = description;
            return this;
        }

        public LoadTestExecuteParameter.Builder thinkTimeMs(long thinkTimeMs) {
            this.thinkTimeMs = thinkTimeMs;
            return this;
        }

        @JsonProperty("stage")
        public LoadTestExecuteParameter.Builder stages(List<Stage> stages) {
            this.stages = stages;
            return this;
        }

        public LoadTestExecuteParameter.Builder scenarios(List<Scenario> scenarios) {
            this.scenarios = scenarios;
            return this;
        }

        public LoadTestExecuteParameter build() {
            return new LoadTestExecuteParameter(this);
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

    public List<Scenario> getScenarios() {
        return scenarios;
    }
}
