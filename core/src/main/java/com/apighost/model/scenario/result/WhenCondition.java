package com.apighost.model.scenario.result;

import java.util.Map;

/**
 * Defines the condition under which a branch is selected,
 * based on status code, response body values, and/or custom conditions.
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class WhenCondition {
    private final String status;
    private final Map<String, Object> body;
    private final String condition;

    private WhenCondition(Builder builder) {
        this.status = builder.status;
        this.body = builder.body;
        this.condition = builder.condition;
    }

    /**
     * Builder
     */
    public static class Builder {
        private String status;
        private Map<String, Object> body;
        private String condition;

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder body(Map<String, Object> body) {
            this.body = body;
            return this;
        }

        public Builder condition(String condition) {
            this.condition = condition;
            return this;
        }

        public WhenCondition build() {
            return new WhenCondition(this);
        }
    }

    /**
     * Getter
     */
    public String getStatus() {
        return status;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public String getCondition() {
        return condition;
    }
}
