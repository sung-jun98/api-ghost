package com.apighost.loadtest;

import com.apighost.loadtest.metrics.LoadTestSnapshotAggregator;
import com.apighost.loadtest.metrics.ResultBuffer;
import com.apighost.loadtest.executor.LoadTestExecutor;
import com.apighost.loadtest.publisher.ResultPublisher;
import com.apighost.loadtest.scheduler.ResultScheduler;
import com.apighost.model.loadtest.parameter.LoadTestExecuteParameter;
import com.apighost.model.loadtest.result.LoadTestSnapshot;
import com.apighost.model.loadtest.result.LoadTestSummary;
import com.apighost.util.time.TimeUtil;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadTestOrchestrator {

    private final ResultBuffer resultBuffer;
    private final ResultPublisher publisher;
    private final ResultScheduler scheduler;
    private final LoadTestSnapshotAggregator snapshotAggregator;
    private final LoadTestExecutor loadTestExecutor;
    private final AtomicInteger currentVus;
    private String startTime;

    public LoadTestOrchestrator(ResultPublisher publisher) {
        this.publisher = publisher;
        this.resultBuffer = new ResultBuffer();
        this.snapshotAggregator = new LoadTestSnapshotAggregator();
        this.currentVus = new AtomicInteger(0);
        this.scheduler = new ResultScheduler(resultBuffer, publisher, snapshotAggregator,
            currentVus::get);
        this.loadTestExecutor = new LoadTestExecutor(resultBuffer, currentVus);
    }

    public void start(LoadTestExecuteParameter parameter) {
        startTime = TimeUtil.getNow();
        scheduler.start(startTime);
        loadTestExecutor.execute(parameter);
        String endTime = TimeUtil.getNow();
        scheduler.shutdown();
        LoadTestSnapshot snapshot = snapshotAggregator.buildSnapshot(startTime, endTime);
        publisher.complete(new LoadTestSummary.Builder()
            .name(parameter.getName())
            .description(parameter.getDescription())
            .startTime(startTime)
            .endTime(endTime)
            .result(snapshot.getResult())
            .endpoints(snapshot.getEndpoints())
            .build());
    }

    public void stop(LoadTestExecuteParameter parameter) {
        String endTime = TimeUtil.getNow();
        scheduler.shutdown();
        LoadTestSnapshot snapshot = snapshotAggregator.buildSnapshot(startTime, endTime);
        publisher.complete(new LoadTestSummary.Builder()
            .name(parameter.getName())
            .description(parameter.getDescription())
            .startTime(startTime)
            .endTime(endTime)
            .result(snapshot.getResult())
            .endpoints(snapshot.getEndpoints())
            .build());
    }
}

