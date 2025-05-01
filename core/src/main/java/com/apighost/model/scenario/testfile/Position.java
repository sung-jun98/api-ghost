package com.apighost.model.scenario.testfile;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * In the GUI, the component X, Y coordinates value
 *
 * @author sung-jun99
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = Position.Builder.class)
public class Position {

    private Integer x;
    private Integer y;

    private Position(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private Integer x;
        private Integer y;

        public Builder x(Integer x) {
            this.x = x;
            return this;
        }

        public Builder y(Integer y) {
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
    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
}
