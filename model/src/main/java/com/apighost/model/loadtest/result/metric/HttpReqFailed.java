package com.apighost.model.loadtest.result.metric;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Number of HTTP requests that failed per second
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = HttpReqFailed.Builder.class)
public class HttpReqFailed {

    private final double rate;
    private final long count;
    private final long fail;

    private HttpReqFailed(Builder builder) {
        this.rate = builder.rate;
        this.count = builder.count;
        this.fail = builder.fail;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private double rate;
        private long count;
        private long fail;

        public Builder rate(double rate) {
            this.rate = rate;
            return this;
        }

        public Builder count(long count) {
            this.count = count;
            return this;
        }

        public Builder fail(long fail) {
            this.fail = fail;
            return this;
        }

        public HttpReqFailed build() {
            return new HttpReqFailed(this);
        }
    }

    /**
     * Getter
     */
    public double getRate() {
        return rate;
    }

    public long getCount() {
        return count;
    }

    public long getFail() {
        return fail;
    }
}
