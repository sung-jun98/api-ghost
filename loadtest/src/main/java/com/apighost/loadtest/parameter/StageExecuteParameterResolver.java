package com.apighost.loadtest.parameter;

/**
 * Utility class for resolving {@link StageExecuteParameter} by applying default values to optional
 * or nullable fields.
 * <p>
 * This ensures that execution-related configurations are complete and consistent before being
 * passed to the load test execution engine.
 * </p>
 *
 * <p>Default values:
 * <ul>
 *     <li>{@code DEFAULT_INCREMENT_INTERVAL_MS} = 100 ms</li>
 *     <li>{@code DEFAULT_THINK_TIME_MS} = 1000 ms</li>
 * </ul>
 * </p>
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class StageExecuteParameterResolver {

    /**
     * The default interval (in milliseconds) between increments of virtual users (VUs).
     */
    public static final long DEFAULT_INCREMENT_INTERVAL_MS = 100L;

    /**
     * The default think time (in milliseconds) between scenario executions.
     */
    public static final long DEFAULT_THINK_TIME_MS = 1000L;

    /**
     * Applies default values to the nullable fields of the given {@link StageExecuteParameter}.
     *
     * <p>This method ensures that {@code incrementIntervalMs} and {@code thinkTimeMs}
     * are set to default values if they are not explicitly defined in the raw input.</p>
     *
     * @param unresolvedParam the raw {@code StageExecuteParameter} instance which may contain null
     *                        values
     * @return a new {@code StageExecuteParameter} instance with defaults applied where necessary
     */
    public static StageExecuteParameter applyDefaults(StageExecuteParameter unresolvedParam) {
        return new StageExecuteParameter.Builder()
            .scenarios(unresolvedParam.getScenarios())
            .startVus(unresolvedParam.getStartVus())
            .targetVus(unresolvedParam.getTargetVus())
            .startTimeMs(unresolvedParam.getStartTimeMs())
            .stageEndTimeMs(unresolvedParam.getStageEndTimeMs())
            .totalDurationMs(unresolvedParam.getTotalDurationMs())
            .stageDurationMs(unresolvedParam.getStageDurationMs())
            .incrementIntervalMs(unresolvedParam.getIncrementIntervalMs() != null
                ? unresolvedParam.getIncrementIntervalMs() : DEFAULT_INCREMENT_INTERVAL_MS)
            .thinkTimeMs(unresolvedParam.getThinkTimeMs() != null
                ? unresolvedParam.getThinkTimeMs() : DEFAULT_THINK_TIME_MS)
            .build();
    }
}
