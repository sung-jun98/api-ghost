package com.apighost.model.scenario;

import com.apighost.model.scenario.Position;
import java.util.List;
import java.util.Map;

public class Step {

    /**
     * HTTP or WEBSOCKET
     */
    private String type;

    /**
     * position of diagram on GUI
     */
    private Position position;

    private Request request;
    private List<Response> response;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }
}
