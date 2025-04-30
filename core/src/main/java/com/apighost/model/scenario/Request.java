package com.apighost.model.scenario;

import java.util.Map;

public class Request {

    /**
     * HTTP methods (GET, POST .etc)
     */
    private String method;

    /**
     * request URL
     */
    private String url;

    /**
     * request header
     */
    private Map<String, String> headers;

    /**
     * request body (field name: {dataType: value})
     */
    private Map<String, Map<String, Object>> body;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, Map<String, Object>> getBody() {
        return body;
    }

    public void setBody(Map<String, Map<String, Object>> body) {
        this.body = body;
    }
}
