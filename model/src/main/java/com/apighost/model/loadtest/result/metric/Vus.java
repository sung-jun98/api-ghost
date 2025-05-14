package com.apighost.model.loadtest.result.metric;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Number of virtual users. Maximum, minimum, at the maximum (the horizontal and vertical axis of
 * the graph of the front) And the current number of virtual users enter.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = Vus.Builder.class)
public class Vus {

    private final int value;
    private final int min;
    private final int max;

    private Vus(Builder builder) {
        this.value = builder.value;
        this.min = builder.min;
        this.max = builder.max;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private int value;
        private int min;
        private int max;

        public Builder value(int value) {
            this.value = value;
            return this;
        }

        public Builder min(int min) {
            this.min = min;
            return this;
        }

        public Builder max(int max) {
            this.max = max;
            return this;
        }

        public Vus build() {
            return new Vus(this);
        }
    }

    /**
     * Getter
     */
    public int getValue() {
        return value;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}
