package com.apighost.loadtest.executor;

import com.apighost.loadtest.metrics.ResultBuffer;
import com.apighost.loadtest.parameter.StageExecuteParameter;
import com.apighost.loadtest.parameter.StageExecuteParameterResolver;
import com.apighost.loadtest.lifecycle.VuLifecycleManager;
import com.apighost.loadtest.validator.LoadTestValidator;
import com.apighost.model.scenario.Scenario;
import com.apighost.model.scenario.ScenarioResult;
import com.apighost.scenario.callback.ScenarioResultCallback;
import com.apighost.scenario.executor.ScenarioTestExecutor;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RampUpStageExecutor implements StageExecutor {

    private final ExecutorService virtualExecutor;
    private final ResultBuffer resultBuffer;
    private final ScenarioTestExecutor scenarioTestExecutor;
    private final AtomicInteger currentVus;
    private final VuLifecycleManager vuLifecycleManager;

    public RampUpStageExecutor(ExecutorService virtualExecutor, ResultBuffer resultBuffer,
        ScenarioTestExecutor scenarioTestExecutor,VuLifecycleManager vuLifecycleManager, AtomicInteger currentVus) {
        this.virtualExecutor = virtualExecutor;
        this.resultBuffer = resultBuffer;
        this.scenarioTestExecutor = scenarioTestExecutor;
        this.vuLifecycleManager = vuLifecycleManager;
        this.currentVus = currentVus;
    }

    @Override
    public void execute(StageExecuteParameter parameter,
        CompletableFuture<Boolean> stageCompletion) {
        StageExecuteParameter resolvedParameter = StageExecuteParameterResolver.applyDefaults(
            parameter);
        LoadTestValidator.validateStageExecutorParameter(resolvedParameter);

        int totalTicks = (int) Math.ceil((double) resolvedParameter.getStageDurationMs() / resolvedParameter.getIncrementIntervalMs());
        int vusPerTick = (int) Math.ceil((double) Math.abs(resolvedParameter.getTargetVus() - resolvedParameter.getStartVus()) / totalTicks);

        if (resolvedParameter.getIsIncreasingVus()) {
            scheduleRampUp(resolvedParameter, vusPerTick, stageCompletion);
        } else {
            scheduleRampDown(resolvedParameter, vusPerTick, stageCompletion);
        }
    }

    private void scheduleRampUp(StageExecuteParameter parameter, int vusPerTick,
        CompletableFuture<Boolean> stageCompletion) {

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            if (System.currentTimeMillis() >= parameter.getStageEndTimeMs()) {
                stageCompletion.complete(true);
                scheduler.shutdownNow();
                return;
            }

            int activeVus = currentVus.get();
            if (activeVus >= parameter.getTargetVus()) {
                return;
            }

            int remainVus = parameter.getTargetVus() - activeVus;
            int vusToSpawnThisTick = Math.min(vusPerTick, remainVus);
            for (int i = 0; i < vusToSpawnThisTick; i++) {
                Future<?> future = virtualExecutor.submit(() ->
                    executeScenarioWorker(parameter.getScenarios(), parameter.getTotalEndTimeMs(), parameter.getThinkTimeMs(), parameter.getTargetVus())
                );
                vuLifecycleManager.add(future);
            }

        }, 0, parameter.getIncrementIntervalMs(), TimeUnit.MILLISECONDS);
    }

    private void scheduleRampDown(StageExecuteParameter parameter, int vusPerTick,
        CompletableFuture<Boolean> stageCompletion) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            if (System.currentTimeMillis() >= parameter.getStageEndTimeMs()) {
                scheduler.shutdownNow();
                stageCompletion.complete(true);
                return;
            }

            int activeVus = currentVus.get();
            if (activeVus <= parameter.getTargetVus()) {
                return;
            }

            int vusToStopThisTick = Math.min(vusPerTick, activeVus - parameter.getTargetVus());
            int vusToReduceThisTick = vuLifecycleManager.cancel(vusToStopThisTick);
            currentVus.addAndGet(-vusToReduceThisTick);
        }, 0, parameter.getIncrementIntervalMs(), TimeUnit.MILLISECONDS);
    }

    private void executeScenarioWorker(List<Scenario> scenarios, long endTimeMs, long thinkTimeMs,
        int targetVus) {
        int activeVus = currentVus.updateAndGet(prev -> (prev < targetVus) ? prev + 1 : prev);
        if (activeVus > targetVus) {
            return;
        }

        while (System.currentTimeMillis() < endTimeMs) {
            for (Scenario s : scenarios) {
                ScenarioResult result = scenarioTestExecutor.execute(s,
                    new ScenarioResultCallback() {
                    });
                resultBuffer.add(result);
                try {
                    Thread.sleep(thinkTimeMs);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
