package com.apighost.model.scenario.result;

import com.apighost.model.scenario.step.HTTPMethod;
import com.apighost.model.scenario.step.ProtocolType;

import com.apighost.model.scenario.request.RequestBody;
import com.apighost.model.scenario.step.Route;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import java.util.Map;

/**
 * Represents the result of an individual test step within a scenario.
 *
 * @author haazz
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = ResultStep.Builder.class)
public class ResultStep {

    private final String stepName;
    private final ProtocolType type;
    private final HTTPMethod method;
    private final String url;
    private final Map<String, String> requestHeader;
    private final RequestBody requestBody;
    private final int status;
    private final Map<String, String> responseHeaders;
    private final String responseBody;
    private final String startTime;
    private final String endTime;
    private final long durationMs;
    private final boolean isRequestSuccess;
    private final List<Route> route;
    private final String nextStep;

    private ResultStep(Builder builder) {
        this.stepName = builder.stepName;
        this.type = builder.type;
        this.url = builder.url;
        this.method = builder.method;
        this.requestBody = builder.requestBody;
        this.requestHeader = builder.requestHeader;
        this.responseBody = builder.responseBody;
        this.responseHeaders = builder.responseHeaders;
        this.status = builder.status;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.durationMs = builder.durationMs;
        this.isRequestSuccess = builder.isRequestSuccess;
        this.route = builder.route;
        this.nextStep = builder.nextStep;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private String stepName;
        private ProtocolType type;
        private String url;
        private HTTPMethod method;
        private RequestBody requestBody;
        private Map<String, String> requestHeader;
        private String responseBody;
        private Map<String, String> responseHeaders;
        private int status;
        private String startTime;
        private String endTime;
        private long durationMs;
        private boolean isRequestSuccess;
        private List<Route> route;
        private String nextStep;

        public Builder stepName(String stepName) {
            this.stepName = stepName;
            return this;
        }

        public Builder type(ProtocolType type) {
            this.type = type;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder method(HTTPMethod method) {
            this.method = method;
            return this;
        }

        public Builder requestBody(RequestBody requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public Builder requestHeader(Map<String, String> requestHeader) {
            this.requestHeader = requestHeader;
            return this;
        }

        public Builder responseBody(String responseBody) {
            this.responseBody = responseBody;
            return this;
        }

        public Builder responseHeaders(Map<String, String> responseHeaders) {
            this.responseHeaders = responseHeaders;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder startTime(String startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(String endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder durationMs(long durationMs) {
            this.durationMs = durationMs;
            return this;
        }

        public Builder isRequestSuccess(boolean isRequestSuccess) {
            this.isRequestSuccess = isRequestSuccess;
            return this;
        }

        public Builder route(List<Route> route) {
            this.route = route;
            return this;
        }

        public ResultStep build() {
            return new ResultStep(this);
        }

        public Builder nextStep(String nextStep) {
            this.nextStep = nextStep;
            return this;
        }
    }

    /**
     * Getter
     */
    public String getStepName() {
        return stepName;
    }

    public ProtocolType getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public HTTPMethod getMethod() {
        return method;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public Map<String, String> getRequestHeader() {
        return requestHeader;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public int getStatus() {
        return status;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public boolean getIsRequestSuccess() {
        return isRequestSuccess;
    }

    public List<Route> getRoute() {
        return route;
    }

    public String getNextStep() {
        return nextStep;
    }
}
