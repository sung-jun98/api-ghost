package com.apighost.model.scenario.test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * In the GUI, the component X, Y coordinates value
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = Position.Builder.class)
public class Position {

    private Float x;
    private Float y;

    private Position(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private Float x;
        private Float y;

        public Builder x(Float x) {
            this.x = x;
            return this;
        }

        public Builder y(Float y) {
            this.y = y;
            return this;
        }

        public Position build() {
            return new Position(this);
        }
    }

    /**
     * Getter
     */
    public Float getX() {
        return x;
    }

    public Float getY() {
        return y;
    }
}
