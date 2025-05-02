package com.apighost.model.scenario.result;

import com.apighost.model.scenario.HTTPMethod;
import com.apighost.model.scenario.ProtocolType;

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
@JsonDeserialize(builder = StepResult.Builder.class)
public class StepResult {

    private final String stepName;
    private final ProtocolType type;
    private final HTTPMethod method;
    private final String url;
    private final Map<String, String> requestHeader;
    private final Map<String, Object> requestBody;
    private final int status;
    private final Map<String, String> responseHeaders;
    private final Map<String, Object> responseBody;
    private final String startTime;
    private final String endTime;
    private final int durationMs;
    private final boolean isRequestSuccess;
    private final List<ResponseBranch> response;

    private StepResult(Builder builder) {
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
        this.response = builder.response;
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
        private Map<String, Object> requestBody;
        private Map<String, String> requestHeader;
        private Map<String, Object> responseBody;
        private Map<String, String> responseHeaders;
        private int status;
        private String startTime;
        private String endTime;
        private int durationMs;
        private boolean isRequestSuccess;
        private List<ResponseBranch> response;

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

    public ProtocolType getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public HTTPMethod getMethod() {
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

    public int getStatus() {
        return status;
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
