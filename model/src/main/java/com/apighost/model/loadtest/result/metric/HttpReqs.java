package com.apighost.model.loadtest.result.metric;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Number of HTTP requests sent to servers per second
 *
 *  @author sung-jun98
 *  @version BETA-0.0.1
 */
@JsonDeserialize(builder = HttpReqs.Builder.class)
public class HttpReqs {

    private final double rate;
    private final int count;

    private HttpReqs(Builder builder) {
        this.rate = builder.rate;
        this.count = builder.count;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private double rate;
        private int count;

        public Builder rate(double rate) {
            this.rate = rate;
            return this;
        }

        public Builder count(int count) {
            this.count = count;
            return this;
        }

        public HttpReqs build() {
            return new HttpReqs(this);
        }
    }

    /** getters*/
    public double getRate() {
        return rate;
    }

    public int getCount() {
        return count;
    }
}
