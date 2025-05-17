package com.apighost.loadtest.parameter;

public class StageExecuteParameterResolver {

    public static final long DEFAULT_INCREMENT_INTERVAL_MS = 100L;
    public static final long DEFAULT_THINK_TIME_MS = 1000L;

    public static StageExecuteParameter applyDefaults(StageExecuteParameter raw) {
        return new StageExecuteParameter.Builder()
            .scenarios(raw.getScenarios())
            .startVus(raw.getStartVus())
            .targetVus(raw.getTargetVus())
            .startTimeMs(raw.getStartTimeMs())
            .stageEndTimeMs(raw.getStageEndTimeMs())
            .totalDurationMs(raw.getTotalDurationMs())
            .stageDurationMs(raw.getStageDurationMs())
            .incrementIntervalMs(raw.getIncrementIntervalMs() != null
                ? raw.getIncrementIntervalMs() : DEFAULT_INCREMENT_INTERVAL_MS)
            .thinkTimeMs(raw.getThinkTimeMs() != null
                ? raw.getThinkTimeMs() : DEFAULT_THINK_TIME_MS)
            .build();
    }
}
