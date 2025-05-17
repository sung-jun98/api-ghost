package com.apighost.loadtest.scheduler;

import com.apighost.loadtest.metrics.LoadTestSnapshotAggregator;
import com.apighost.loadtest.metrics.ResultBuffer;
import com.apighost.loadtest.publisher.ResultPublisher;
import com.apighost.model.loadtest.result.LoadTestSnapshot;
import com.apighost.model.scenario.ScenarioResult;
import com.apighost.util.file.TimeUtils;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Periodically aggregates and publishes load test results at a fixed interval.
 *
 * <p>
 * This class is responsible for draining {@link ScenarioResult}s from the {@link ResultBuffer},
 * computing a {@link LoadTestSnapshot} via {@link LoadTestSnapshotAggregator}, and publishing it
 * using a {@link ResultPublisher}. It runs asynchronously in a scheduled thread executor.
 * </p>
 *
 * <p>
 * The interval for result publishing defaults to 5 seconds but can be customized via constructor.
 * </p>
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class ResultScheduler {

    private static final Duration DEFAULT_INTERVAL = Duration.ofSeconds(5);

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final ResultBuffer buffer;
    private final ResultPublisher publisher;
    private final Duration interval;
    private final LoadTestSnapshotAggregator snapshotAggregator;
    private final Supplier<Integer> currentVusSupplier;

    /**
     * Constructs a scheduler with the default interval (5 seconds).
     *
     * @param buffer             the result buffer to drain data from
     * @param publisher          the publisher to publish snapshots to
     * @param snapshotAggregator the aggregator used to build snapshots
     * @param currentVusSupplier supplier providing the current number of VUs
     */
    public ResultScheduler(ResultBuffer buffer, ResultPublisher publisher,
        LoadTestSnapshotAggregator snapshotAggregator,
        Supplier<Integer> currentVusSupplier) {
        this.buffer = buffer;
        this.publisher = publisher;
        this.snapshotAggregator = snapshotAggregator;
        this.currentVusSupplier = currentVusSupplier;
        this.interval = DEFAULT_INTERVAL;
    }

    /**
     * Constructs a scheduler with a custom interval.
     *
     * @param buffer             the result buffer to drain data from
     * @param publisher          the publisher to publish snapshots to
     * @param snapshotAggregator the aggregator used to build snapshots
     * @param currentVusSupplier supplier providing the current number of VUs
     * @param interval           the interval between scheduled executions
     */
    public ResultScheduler(ResultBuffer buffer, ResultPublisher publisher,
        LoadTestSnapshotAggregator snapshotAggregator,
        Supplier<Integer> currentVusSupplier,
        Duration interval) {
        this.buffer = buffer;
        this.publisher = publisher;
        this.snapshotAggregator = snapshotAggregator;
        this.currentVusSupplier = currentVusSupplier;
        this.interval = interval;
    }

    /**
     * Starts the periodic scheduling of result aggregation and publishing.
     *
     * @param startTime the start time of the load test (formatted timestamp string)
     */
    public void start(String startTime) {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                List<ScenarioResult> batch = buffer.drain();
                int currentVus = currentVusSupplier.get();

                if (batch != null) {
                    snapshotAggregator.accumulate(batch, currentVus);
                    String timestamp = TimeUtils.getNow();
                    LoadTestSnapshot snapshot = snapshotAggregator.buildSnapshot(startTime,
                        timestamp);
                    publisher.publish(snapshot);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, interval.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * Gracefully shuts down the scheduled result publishing task.
     */
    public void shutdown() {
        scheduler.shutdown();
    }
}
