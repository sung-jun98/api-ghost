package com.apighost.model.scenario;

import java.util.Map;

public class When {

    /**
     * status of response ex. "200-299" or 200
     */
    private Object status;
    private Map<String, Object> body;
    /**
     * Conditional expression (ex: "${response.body.posts.length} == 0")
     */
    private String condition;

    private When(Builder builder) {
        this.status = builder.status;
        this.body = builder.body;
        this.condition = builder.condition;
    }

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
