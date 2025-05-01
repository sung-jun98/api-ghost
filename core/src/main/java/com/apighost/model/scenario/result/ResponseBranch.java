package com.apighost.model.scenario.result;

/**
 * Represents a conditional branch based on the response of a request.
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class ResponseBranch {

    private final WhenCondition when;
    private final ThenAction then;

    private ResponseBranch(Builder builder) {
        this.when = builder.when;
        this.then = builder.then;
    }

    /**
     * Builder
     */
    public static class Builder {

        private WhenCondition when;
        private ThenAction then;

        public Builder when(WhenCondition when) {
            this.when = when;
            return this;
        }

        public Builder then(ThenAction then) {
            this.then = then;
            return this;
        }

        public ResponseBranch build() {
            return new ResponseBranch(this);
        }
    }

    /**
     * Getter
     */
    public WhenCondition getWhen() {
        return when;
    }

    public ThenAction getThen() {
        return then;
    }
}
