package com.apighost.model.loadtest.result.metric;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * The number of test repetitions completed per second
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = Iterations.Builder.class)
public class Iterations {

    private final double rate;
    private final long count;

    private Iterations(Builder builder) {
        this.rate = builder.rate;
        this.count = builder.count;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private double rate;
        private long count;

        public Builder rate(double rate) {
            this.rate = rate;
            return this;
        }

        public Builder count(long count) {
            this.count = count;
            return this;
        }

        public Iterations build() {
            return new Iterations(this);
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
}
