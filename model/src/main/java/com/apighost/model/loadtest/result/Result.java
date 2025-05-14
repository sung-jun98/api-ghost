package com.apighost.model.loadtest.result;

import com.apighost.model.loadtest.result.metric.HttpReqDuration;
import com.apighost.model.loadtest.result.metric.HttpReqFailed;
import com.apighost.model.loadtest.result.metric.HttpReqs;
import com.apighost.model.loadtest.result.metric.Iterations;
import com.apighost.model.loadtest.result.metric.Vus;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classes containing practical load test results
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = Result.Builder.class)
public class Result {

    private final Iterations iterations;

    @JsonProperty("http_reqs")
    private final HttpReqs httpReqs;

    @JsonProperty("http_req_duration")
    private final HttpReqDuration httpReqDuration;

    @JsonProperty("http_req_failed")
    private final HttpReqFailed httpReqFailed;

    private final Vus vus;

    private Result(Builder builder) {
        this.iterations = builder.iterations;
        this.httpReqs = builder.httpReqs;
        this.httpReqDuration = builder.httpReqDuration;
        this.httpReqFailed = builder.httpReqFailed;
        this.vus = builder.vus;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private Iterations iterations;
        private HttpReqs httpReqs;
        private HttpReqDuration httpReqDuration;
        private HttpReqFailed httpReqFailed;
        private Vus vus;

        public Builder iterations(Iterations iterations) {
            this.iterations = iterations;
            return this;
        }

        @JsonProperty("http_reqs")
        public Builder httpReqs(HttpReqs httpReqs) {
            this.httpReqs = httpReqs;
            return this;
        }

        @JsonProperty("http_req_duration")
        public Builder httpReqDuration(HttpReqDuration httpReqDuration) {
            this.httpReqDuration = httpReqDuration;
            return this;
        }

        @JsonProperty("http_req_failed")
        public Builder httpReqFailed(HttpReqFailed httpReqFailed) {
            this.httpReqFailed = httpReqFailed;
            return this;
        }

        public Builder vus(Vus vus) {
            this.vus = vus;
            return this;
        }

        public Result build() {
            return new Result(this);
        }
    }

    /**
     * Getter
     */
    public Iterations getIterations() {
        return iterations;
    }

    @JsonProperty("http_reqs")
    public HttpReqs getHttpReqs() {
        return httpReqs;
    }

    @JsonProperty("http_req_duration")
    public HttpReqDuration getHttpReqDuration() {
        return httpReqDuration;
    }

    @JsonProperty("http_req_failed")
    public HttpReqFailed getHttpReqFailed() {
        return httpReqFailed;
    }

    public Vus getVus() {
        return vus;
    }
}
