package com.apighost.model.scenario.testfile;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.Map;

/**
 * Conditions of quarterly inquiries
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = When.Builder.class)
public class When {

    private Object status;
    private Map<String, Object> body;

    private String condition;


    private When(Builder builder) {
        this.status = builder.status;
        this.body = builder.body;
        this.condition = builder.condition;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private Object status;
        private Map<String, Object> body;
        private String condition;

        public Builder status(Object status) {
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

        public When build() {
            return new When(this);
        }
    }

    /**
     * Getter
     */
    public Object getStatus() {
        return status;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public String getCondition() {
        return condition;
    }
}
