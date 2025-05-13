package com.apighost.model.loadtest.parameter;

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

    private final int vus;
    private final int duration; // in seconds

    private Stage(Builder builder) {
        this.vus = builder.vus;
        this.duration = builder.duration;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private int vus;
        private int duration;

        public Builder vus(int vus) {
            this.vus = vus;
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Stage build() {
            return new Stage(this);
        }
    }

    /**
     * getters
     */
    public int getVus() {
        return vus;
    }

    public int getDuration() {
        return duration;
    }
}
