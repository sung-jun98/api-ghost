package com.apighost.model.scenario.test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.Map;

/**
 * Define conditions that will happen after satisfying the conditions
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = Then.Builder.class)
public class Then {

    private Map<String, String> store;
    private String step;

    private Then(Builder builder) {
        this.store = builder.store;
        this.step = builder.step;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private Map<String, String> store;
        private String step;

        public Builder store(Map<String, String> store) {
            this.store = store;
            return this;
        }

        public Builder step(String step) {
            this.step = step;
            return this;
        }

        public Then build() {
            return new Then(this);
        }
    }

    /**
     * Getter
     */
    public Map<String, String> getStore() {
        return store;
    }

    public String getStep() {
        return step;
    }
}
