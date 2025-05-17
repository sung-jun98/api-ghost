package com.apighost.loadtest.scheduler;

import com.apighost.loadtest.metrics.LoadTestSnapshotAggregator;
import com.apighost.loadtest.metrics.ResultBuffer;
import com.apighost.loadtest.publisher.ResultPublisher;
import com.apighost.model.loadtest.result.LoadTestSnapshot;
import com.apighost.model.scenario.ScenarioResult;
import com.apighost.util.time.TimeUtil;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class ResultScheduler {

    private static final Duration DEFAULT_INTERVAL = Duration.ofSeconds(5);

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final ResultBuffer buffer;
    private final ResultPublisher publisher;
    private final Duration interval;
    private final LoadTestSnapshotAggregator snapshotAggregator;
    private final Supplier<Integer> currentVusSupplier;

    public ResultScheduler(ResultBuffer buffer, ResultPublisher publisher,
        LoadTestSnapshotAggregator snapshotAggregator, Supplier<Integer> currentVusSupplier) {
        this.buffer = buffer;
        this.publisher = publisher;
        this.snapshotAggregator = snapshotAggregator;
        this.currentVusSupplier = currentVusSupplier;
        this.interval = DEFAULT_INTERVAL;
    }

    public ResultScheduler(ResultBuffer buffer, ResultPublisher publisher,
        LoadTestSnapshotAggregator snapshotAggregator, Supplier<Integer> currentVusSupplier,
        Duration interval) {
        this.buffer = buffer;
        this.publisher = publisher;
        this.snapshotAggregator = snapshotAggregator;
        this.currentVusSupplier = currentVusSupplier;
        this.interval = interval;
    }

    public void start(String startTime) {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                List<ScenarioResult> batch = buffer.drain();
                int currentVus = currentVusSupplier.get();

                if (batch != null) {
                    snapshotAggregator.accumulate(batch, currentVus);
                    String timestamp = TimeUtil.getNow();
                    LoadTestSnapshot snapshot = snapshotAggregator.buildSnapshot(startTime,
                        timestamp);
                    publisher.publish(snapshot);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, interval.toMillis(), TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}