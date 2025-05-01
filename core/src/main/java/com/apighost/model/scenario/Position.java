package com.apighost.model.scenario;

public class Position {

    private Integer x;
    private Integer y;

    private Position(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
    }

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

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
}
