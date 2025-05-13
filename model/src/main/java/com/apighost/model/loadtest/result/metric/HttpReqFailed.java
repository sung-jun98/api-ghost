package com.apighost.model.loadtest.result.metric;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Number of HTTP requests that failed per second
 *
 *  @author sung-jun98
 *  @version BETA-0.0.1
 */
@JsonDeserialize(builder = HttpReqFailed.Builder.class)
public class HttpReqFailed {

    private final double rate;
    private final int passes;
    private final int fails;

    private HttpReqFailed(Builder builder) {
        this.rate = builder.rate;
        this.passes = builder.passes;
        this.fails = builder.fails;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private double rate;
        private int passes;
        private int fails;

        public Builder rate(double rate) {
            this.rate = rate;
            return this;
        }

        public Builder passes(int passes) {
            this.passes = passes;
            return this;
        }

        public Builder fails(int fails) {
            this.fails = fails;
            return this;
        }

        public HttpReqFailed build() {
            return new HttpReqFailed(this);
        }
    }

    /** getters */
    public double getRate() {
        return rate;
    }

    public int getPasses() {
        return passes;
    }

    public int getFails() {
        return fails;
    }
}
