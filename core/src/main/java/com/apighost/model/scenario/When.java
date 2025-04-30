package com.apighost.model.scenario;

import java.util.Map;

public class When {

    /**
     * status of response ex. "200-299" or 200
     */
    private Object status;

    private Map<String, Object> body;

    /**
     * Conditional expression (ex: "${response.body.posts.length} == 0")
     */
    private String condition;

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
