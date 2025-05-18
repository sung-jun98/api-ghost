package com.apighost.loadtest.metrics;

import com.apighost.model.loadtest.result.Endpoint;
import com.apighost.model.loadtest.result.LoadTestSnapshot;
import com.apighost.model.loadtest.result.Result;
import com.apighost.model.scenario.ScenarioResult;
import com.apighost.model.scenario.result.ResultStep;
import com.apighost.util.file.TimeUtils;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Aggregates {@link ScenarioResult} data over time to build snapshots of load test results.
 *
 * <p>This class collects scenario execution results and generates a summary snapshot, including
 * metrics for all endpoints and total aggregated results.</p>
 *
 * <p>Thread-safe using {@link CopyOnWriteArrayList} to support concurrent updates during load
 * testing.</p>
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class LoadTestSnapshotAggregator {

    private final List<ScenarioResult> allScenarioResults = new CopyOnWriteArrayList<>();
    private int currentVus = 0;

    /**
     * Accumulates a batch of {@link ScenarioResult} instances and updates the current VU count.
     *
     * @param batch a list of scenario results to be added
     * @param vus   the number of virtual users at the time of accumulation
     */
    public void accumulate(List<ScenarioResult> batch, int vus) {
        allScenarioResults.addAll(batch);
        this.currentVus = vus;
    }

    /**
     * Builds a {@link LoadTestSnapshot} representing the aggregated results at a given timestamp.
     *
     * @param startTime the test start time in string format (used for elapsed time calculation)
     * @param timestamp the current timestamp to associate with the snapshot
     * @return a {@link LoadTestSnapshot} including total result metrics and per-endpoint breakdowns
     */
    public LoadTestSnapshot buildSnapshot(String startTime, String timestamp) {
        long timeDiffSec = TimeUtils.calculateDiffInMs(startTime, timestamp) / 1000;
        List<ResultStep> allSteps = allScenarioResults.stream()
            .flatMap(sr -> sr.getResults().stream())
            .collect(Collectors.toList());

        Result total = MetricCalculator.calculateScenarioResults(allScenarioResults, currentVus,
            timeDiffSec);

        Map<String, List<ResultStep>> endpointMap = allSteps.stream()
            .collect(Collectors.groupingBy(ResultStep::getUrl));

        List<Endpoint> endpoints = endpointMap.entrySet().stream()
            .map(entry -> new Endpoint.Builder()
                .url(entry.getKey())
                .result(MetricCalculator.calculateResultSteps(entry.getValue(), currentVus,
                    timeDiffSec))
                .build())
            .collect(Collectors.toList());

        return new LoadTestSnapshot.Builder()
            .timeStamp(timestamp)
            .result(total)
            .endpoints(endpoints)
            .build();
    }
}

