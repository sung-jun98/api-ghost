package com.apighost.model.scenario;

import com.apighost.model.scenario.Position;
import java.util.List;
import java.util.Map;

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
