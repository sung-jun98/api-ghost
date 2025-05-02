package com.apighost.model.scenario.test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Set the branch and the following request value to be sent to the next endpoint in the endpoint
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = Route.Builder.class)
public class Route {

    private Expected expected;
    private Then then;

    private Route(Builder builder) {
        this.expected = builder.expected;
        this.then = builder.then;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private Expected expected;
        private Then then;

        public Builder expected(Expected expected) {
            this.expected = expected;
            return this;
        }

        public Builder then(Then then) {
            this.then = then;
            return this;
        }

        public Route build() {
            return new Route(this);
        }
    }

    /**
     * Getter
     */
    public Expected getExpected() {
        return expected;
    }

    public Then getThen() {
        return then;
    }
}
