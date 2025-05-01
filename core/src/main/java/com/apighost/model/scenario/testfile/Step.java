package com.apighost.model.scenario.testfile;

import java.util.List;

/**
 * Definition of each step inside the scenario test
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class Step {

    private String type;
    private Position position;
    private Request request;
    private List<Response> response;


    private Step(Builder builder) {
        this.type = builder.type;
        this.position = builder.position;
        this.request = builder.request;
        this.response = builder.response;
    }

    /**
     * Builder
     */
    public static class Builder {

        private String type;
        private Position position;
        private Request request;
        private List<Response> response;

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder position(Position position) {
            this.position = position;
            return this;
        }

        public Builder request(Request request) {
            this.request = request;
            return this;
        }

        public Builder response(List<Response> response) {
            this.response = response;
            return this;
        }

        public Step build() {
            return new Step(this);
        }
    }

    /**
     * Getter
     */
    public String getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    public Request getRequest() {
        return request;
    }

    public List<Response> getResponse() {
        return response;
    }
}
