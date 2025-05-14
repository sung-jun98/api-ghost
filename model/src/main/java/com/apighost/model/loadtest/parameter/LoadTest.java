package com.apighost.model.loadtest.parameter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;

/**
 * It contains the details of the load test setting parameters.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = LoadTest.Builder.class)
public class LoadTest {

    private final long timeoutMs;
    private final long thinkTimeMs;
    private final List<Stage> loadPattern;

    private LoadTest(Builder builder) {
        this.timeoutMs = builder.timeoutMs;
        this.thinkTimeMs = builder.thinkTimeMs;
        this.loadPattern = builder.loadPattern;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private long timeoutMs;
        private long thinkTimeMs;
        private List<Stage> loadPattern;

        public Builder timeoutMs(long timeoutMs) {
            this.timeoutMs = timeoutMs;
            return this;
        }

        public Builder thinkTimeMs(int thinkTimeMs) {
            this.thinkTimeMs = thinkTimeMs;
            return this;
        }

        public Builder loadPattern(List<Stage> loadPattern) {
            this.loadPattern = loadPattern;
            return this;
        }

        public LoadTest build() {
            return new LoadTest(this);
        }
    }


    /**
     * Getter
     *
     */
    public long getTimeoutMs() {
        return timeoutMs;
    }

    public long getThinkTimeMs() {
        return thinkTimeMs;
    }

    public List<Stage> getLoadPattern() {
        return loadPattern;
    }
}
