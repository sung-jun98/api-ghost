package com.apighost.model.loadtest.result.metric;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * HTTP request processing time
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = HttpReqDuration.Builder.class)
public class HttpReqDuration {

    private final long avg;
    private final long min;
    private final long med;
    private final long max;
    @JsonProperty("p(90)")
    private final long p90;

    @JsonProperty("p(95)")
    private final long p95;

    private HttpReqDuration(Builder builder) {
        this.avg = builder.avg;
        this.min = builder.min;
        this.med = builder.med;
        this.max = builder.max;
        this.p90 = builder.p90;
        this.p95 = builder.p95;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private long avg;
        private long min;
        private long med;
        private long max;
        private long p90;
        private long p95;

        public Builder avg(long avg) {
            this.avg = avg;
            return this;
        }

        public Builder min(long min) {
            this.min = min;
            return this;
        }

        public Builder med(long med) {
            this.med = med;
            return this;
        }

        public Builder max(long max) {
            this.max = max;
            return this;
        }

        @JsonProperty("p(90)")
        public Builder p90(long p90) {
            this.p90 = p90;
            return this;
        }

        @JsonProperty("p(95)")
        public Builder p95(long p95) {
            this.p95 = p95;
            return this;
        }

        public HttpReqDuration build() {
            return new HttpReqDuration(this);
        }
    }

    /**
     * Getter
     */
    public long getAvg() {
        return avg;
    }

    public long getMin() {
        return min;
    }

    public long getMed() {
        return med;
    }

    public long getMax() {
        return max;
    }

    @JsonProperty("p(90)")
    public long getP90() {
        return p90;
    }

    @JsonProperty("p(95)")
    public long getP95() {
        return p95;
    }
}
