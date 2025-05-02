package com.apighost.model.scenario;

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

    private double x;
    private double y;

    private Position(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private double x;
        private double y;

        public Builder x(double x) {
            this.x = x;
            return this;
        }

        public Builder y(double y) {
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
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
