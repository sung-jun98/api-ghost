package com.apighost.model.scenario.result;

import java.util.List;
import java.util.Map;

/**
 * Represents the result of an individual test step within a scenario.
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class StepResult {
    private final String stepName;
    private final String type;
    private final String endpoint;
    private final String method;
    private final Map<String, Object> requestBody;
    private final Map<String, String> requestHeader;
    private final Map<String, Object> responseBody;
    private final Map<String, String> responseHeaders;
    private final int statusCode;
    private final String startTime;
    private final String endTime;
    private final int durationMs;
    private final boolean isRequestSuccess;
    private final List<ResponseBranch> response;

    private StepResult(Builder builder) {
        this.stepName = builder.stepName;
        this.type = builder.type;
        this.endpoint = builder.endpoint;
        this.method = builder.method;
        this.requestBody = builder.requestBody;
        this.requestHeader = builder.requestHeader;
        this.responseBody = builder.responseBody;
        this.responseHeaders = builder.responseHeaders;
        this.statusCode = builder.statusCode;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.durationMs = builder.durationMs;
        this.isRequestSuccess = builder.isRequestSuccess;
        this.response = builder.response;
    }

    /**
     * Builder
     */
    public static class Builder {
        private String stepName;
        private String type;
        private String endpoint;
        private String method;
        private Map<String, Object> requestBody;
        private Map<String, String> requestHeader;
        private Map<String, Object> responseBody;
        private Map<String, String> responseHeaders;
        private int statusCode;
        private String startTime;
        private String endTime;
        private int durationMs;
        private boolean isRequestSuccess;
        private List<ResponseBranch> response;

        public Builder stepName(String stepName) {
            this.stepName = stepName;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder requestBody(Map<String, Object> requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public Builder requestHeader(Map<String, String> requestHeader) {
            this.requestHeader = requestHeader;
            return this;
        }

        public Builder responseBody(Map<String, Object> responseBody) {
            this.responseBody = responseBody;
            return this;
        }

        public Builder responseHeaders(Map<String, String> responseHeaders) {
            this.responseHeaders = responseHeaders;
            return this;
        }

        public Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
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

        public Builder durationMs(int durationMs) {
            this.durationMs = durationMs;
            return this;
        }

        public Builder requestSuccess(boolean isRequestSuccess) {
            this.isRequestSuccess = isRequestSuccess;
            return this;
        }

        public Builder response(List<ResponseBranch> response) {
            this.response = response;
            return this;
        }

        public StepResult build() {
            return new StepResult(this);
        }
    }

    /**
     * Getter
     */
    public String getStepName() {
        return stepName;
    }

    public String getType() {
        return type;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, Object> getRequestBody() {
        return requestBody;
    }

    public Map<String, String> getRequestHeader() {
        return requestHeader;
    }

    public Map<String, Object> getResponseBody() {
        return responseBody;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public boolean isRequestSuccess() {
        return isRequestSuccess;
    }

    public List<ResponseBranch> getResponse() {
        return response;
    }
}
