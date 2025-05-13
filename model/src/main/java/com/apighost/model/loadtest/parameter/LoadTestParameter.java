package com.apighost.model.loadtest.parameter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;

/**
 * Settings parametes required for load test
 * It is made of YAML file and will be between DTO and YAML through parser.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = LoadTestParameter.Builder.class)
public class LoadTestParameter {
    private final String name;
    private final String description;
    private final LoadTest loadTest;
    private final List<String> scenarios;

    private LoadTestParameter(Builder builder) {
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
        private List<String> scenarios;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder loadTest(LoadTest loadTest) {
            this.loadTest = loadTest;
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

    /** getters */
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LoadTest getLoadTest() {
        return loadTest;
    }

    public List<String> getScenarios() {
        return scenarios;
    }
}
