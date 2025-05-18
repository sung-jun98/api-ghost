package com.apighost.loadtest.executor;

import com.apighost.loadtest.metrics.ResultBuffer;
import com.apighost.loadtest.parameter.StageExecuteParameter;
import com.apighost.loadtest.lifecycle.VuLifecycleManager;
import com.apighost.model.loadtest.parameter.LoadTestExecuteParameter;
import com.apighost.model.loadtest.parameter.Stage;
import com.apighost.scenario.executor.ScenarioTestExecutor;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadTestExecutor {

    private final ExecutorService virtualExecutor;
    private final ResultBuffer resultBuffer;
    private final ScenarioTestExecutor scenarioTestExecutor;
    private final VuLifecycleManager vuLifecycleManager;
    private final AtomicInteger currentVus;

    public LoadTestExecutor(ResultBuffer resultBuffer, AtomicInteger currentVus) {
        this.resultBuffer = resultBuffer;
        this.currentVus = currentVus;
        this.virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();
        this.vuLifecycleManager = new VuLifecycleManager();
        this.scenarioTestExecutor = new ScenarioTestExecutor();
    }

    public void execute(LoadTestExecuteParameter parameter) {
        int previousVus = 0;
        List<Stage> stages = parameter.getStages();
        long totalDurationMs = parameter.getStages().stream().mapToLong(Stage::getDurationMs).sum();
        long startTimeMs = System.currentTimeMillis();
        long stageEndTimeMs = startTimeMs;

        for (Stage stage : stages) {
            CompletableFuture<Boolean> stageCompletion = new CompletableFuture<>();
            StageExecutor stageExecutor;
            stageExecutor = new RampUpStageExecutor(virtualExecutor, resultBuffer,
                scenarioTestExecutor, vuLifecycleManager, currentVus);
            stageEndTimeMs += stage.getDurationMs();
            stageExecutor.execute(new StageExecuteParameter.Builder()
                .scenarios(parameter.getScenarios())
                .startVus(previousVus)
                .targetVus(stage.getVus())
                .startTimeMs(startTimeMs)
                .totalDurationMs(totalDurationMs)
                .stageDurationMs(stage.getDurationMs())
                .stageEndTimeMs(stageEndTimeMs)
                .thinkTimeMs(parameter.getThinkTimeMs())
                .build(), stageCompletion);
            stageCompletion.join();
            previousVus = stage.getVus();
        }

        vuLifecycleManager.cancelAll();
        virtualExecutor.shutdown();
    }
}
