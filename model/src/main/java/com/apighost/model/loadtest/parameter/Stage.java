package com.apighost.model.loadtest.parameter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * You can guess the type of test by setting a VUS (virtual user) during the period and during that
 * period and adding it to the Loadtest parameter settings.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = Stage.Builder.class)
public class Stage {

    @JsonProperty("vus")
    private final int vus;
    @JsonProperty("duration")
    private final long durationMs;

    private Stage(Builder builder) {
        this.vus = builder.vus;
        this.durationMs = builder.durationMs;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private int vus;
        private long durationMs;

        @JsonProperty("vus")
        public Builder vus(int vus) {
            this.vus = vus;
            return this;
        }

        @JsonProperty("duration")
        public Builder durationMs(long durationMs) {
            this.durationMs = durationMs;
            return this;
        }

        public Stage build() {
            return new Stage(this);
        }
    }

    /**
     * Getter
     */
    @JsonProperty("vus")
    public int getVus() {
        return vus;
    }

    @JsonProperty("duration")
    public long getDurationMs() {
        return durationMs;
    }
}
