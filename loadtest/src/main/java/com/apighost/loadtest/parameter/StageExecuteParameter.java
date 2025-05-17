package com.apighost.loadtest.parameter;

import com.apighost.model.scenario.Scenario;
import java.util.List;

/**
 * Represents parameters for executing a load test stage.
 * <p>
 * Fields like {@code incrementIntervalMs} are optional. If not provided, defaults will apply.
 * </p>
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class StageExecuteParameter {

    private final List<Scenario> scenarios;
    private final int startVus;
    private final int targetVus;
    private final long startTimeMs;
    private final long totalEndTimeMs;
    private final long stageEndTimeMs;
    private final long totalDurationMs;
    private final long stageDurationMs;
    private final Long incrementIntervalMs;
    private final Long thinkTimeMs;
    private final boolean isIncreasingVus;

    private StageExecuteParameter(Builder builder) {
        this.scenarios = builder.scenarios;
        this.targetVus = builder.targetVus;
        this.totalDurationMs = builder.totalDurationMs;
        this.stageDurationMs = builder.stageDurationMs;
        this.startVus = builder.startVus;
        this.thinkTimeMs = builder.thinkTimeMs;
        this.incrementIntervalMs = builder.incrementIntervalMs;
        this.isIncreasingVus = targetVus > startVus;
        this.startTimeMs = builder.startTimeMs;
        this.totalEndTimeMs = startTimeMs + totalDurationMs;
        this.stageEndTimeMs = builder.stageEndTimeMs;
    }

    /**
     * Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private List<Scenario> scenarios;
        private int startVus;
        private int targetVus;
        private long startTimeMs;
        private long stageEndTimeMs;
        private long totalDurationMs;
        private long stageDurationMs;
        private Long incrementIntervalMs;
        private Long thinkTimeMs;

        public Builder scenarios(List<Scenario> scenarios) {
            this.scenarios = scenarios;
            return this;
        }

        public Builder startVus(int startVus) {
            this.startVus = startVus;
            return this;
        }

        public Builder targetVus(int targetVus) {
            this.targetVus = targetVus;
            return this;
        }

        public Builder totalDurationMs(long totalDurationMs) {
            this.totalDurationMs = totalDurationMs;
            return this;
        }

        public Builder stageDurationMs(long stageDurationMs) {
            this.stageDurationMs = stageDurationMs;
            return this;
        }

        public Builder incrementIntervalMs(Long incrementIntervalMs) {
            this.incrementIntervalMs = incrementIntervalMs;
            return this;
        }

        public Builder thinkTimeMs(Long thinkTimeMs) {
            this.thinkTimeMs = thinkTimeMs;
            return this;
        }

        public Builder startTimeMs(long startTimeMs) {
            this.startTimeMs = startTimeMs;
            return this;
        }

        public Builder stageEndTimeMs(long stageEndTimeMs) {
            this.stageEndTimeMs = stageEndTimeMs;
            return this;
        }

        public StageExecuteParameter build() {
            return new StageExecuteParameter(this);
        }
    }

    /**
     * Getter
     */
    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public int getStartVus() {
        return startVus;
    }

    public int getTargetVus() {
        return targetVus;
    }

    public long getStartTimeMs() {
        return startTimeMs;
    }

    public long getTotalEndTimeMs() {
        return totalEndTimeMs;
    }

    public long getStageEndTimeMs() {
        return stageEndTimeMs;
    }

    public long getTotalDurationMs() {
        return totalDurationMs;
    }

    public long getStageDurationMs() {
        return stageDurationMs;
    }

    public Long getIncrementIntervalMs() {
        return incrementIntervalMs;
    }

    public Long getThinkTimeMs() {
        return thinkTimeMs;
    }

    public boolean getIsIncreasingVus() {
        return isIncreasingVus;
    }
}

