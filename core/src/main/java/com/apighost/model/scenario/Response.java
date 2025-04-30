package com.apighost.model.scenario;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public class Response {

    /**
     * Conditions for branch
     */
    private When when;

    /**
     * After satisfaction with branch conditions
     */
    private Then then;

    public When getWhen() {
        return when;
    }

    public void setWhen(When when) {
        this.when = when;
    }

    public Then getThen() {
        return then;
    }

    public void setThen(Then then) {
        this.then = then;
    }
}
