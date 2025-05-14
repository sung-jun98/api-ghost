package com.apighost.model.loadtest.parameter;

import com.apighost.model.scenario.Scenario;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;

@JsonDeserialize(builder = LoadTestExecuteParameter.Builder.class)
public class LoadTestExecuteParameter {

    private final String name;
    private final String description;
    private final LoadTest loadTest;
    private final List<Scenario> scenarios;

    private LoadTestExecuteParameter(LoadTestExecuteParameter.Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.loadTest = builder.loadTest;
        this.scenarios = builder.scenarios;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private String name;
        private String description;
        private LoadTest loadTest;
        private List<Scenario> scenarios;

        public LoadTestExecuteParameter.Builder name(String name) {
            this.name = name;
            return this;
        }

        public LoadTestExecuteParameter.Builder description(String description) {
            this.description = description;
            return this;
        }

        public LoadTestExecuteParameter.Builder loadTest(LoadTest loadTest) {
            this.loadTest = loadTest;
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

    public LoadTest getLoadTest() {
        return loadTest;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }
}
