package com.apighost.model.scenario;

import java.util.List;
import java.util.Map;

/**
 * Set the branch and the following request value to be sent to the next endpoint in the endpoint
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class Response {

    private When when;
    private Then then;

    private Response(Builder builder) {
        this.when = builder.when;
        this.then = builder.then;
    }

    /**
     * Builder
     */
    public static class Builder {

        private When when;
        private Then then;

        public Builder when(When when) {
            this.when = when;
            return this;
        }

        public Builder then(Then then) {
            this.then = then;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }

    /**
     * Getter
     */
    public When getWhen() {
        return when;
    }

    public Then getThen() {
        return then;
    }
}
