package com.apighost.model.scenario.step;

import com.apighost.model.scenario.request.Request;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;

/**
 * Definition of each step inside the scenario test
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = Step.Builder.class)
public class Step {

    private ProtocolType type;
    private Position position;
    private Request request;
    private List<Route> route;


    private Step(Builder builder) {
        this.type = builder.type;
        this.position = builder.position;
        this.request = builder.request;
        this.route = builder.route;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private ProtocolType type;
        private Position position;
        private Request request;
        private List<Route> route;

        public Builder type(ProtocolType type) {
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

        public Builder route(List<Route> route) {
            this.route = route;
            return this;
        }

        public Step build() {
            return new Step(this);
        }
    }

    /**
     * Getter
     */
    public ProtocolType getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    public Request getRequest() {
        return request;
    }

    public List<Route> getRoute() {
        return route;
    }
}
