package com.apighost.loadtest.metrics;

import com.apighost.model.loadtest.result.EndpointResult;
import com.apighost.model.loadtest.result.Result;
import com.apighost.model.loadtest.result.metric.HttpReqDuration;
import com.apighost.model.loadtest.result.metric.HttpReqFailed;
import com.apighost.model.loadtest.result.metric.HttpReqs;
import com.apighost.model.loadtest.result.metric.Iterations;
import com.apighost.model.scenario.ScenarioResult;
import com.apighost.model.scenario.result.ResultStep;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Utility class for calculating metrics from scenario or step-level results during load test
 * execution.
 *
 * <p>
 * This class provides methods to compute aggregated statistics such as request counts, latency
 * distributions (avg, min, max, median, p90, p95), and failure rates based on the raw results of
 * load test scenarios or endpoints.
 * </p>
 *
 * <p>
 * It supports both overall scenario-level aggregation and endpoint-specific step-based
 * calculations.
 * </p>
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class MetricCalculator {

    /**
     * Calculates load test metrics from a list of {@link ScenarioResult}s.
     *
     * <p>
     * Aggregates all steps across all scenarios and computes metrics such as average latency,
     * percentiles, failure rate, and throughput.
     * </p>
     *
     * @param scenarioResults the list of scenario execution results
     * @param currentVus      the number of virtual users at the time of calculation
     * @param elapsedTimeSec  total elapsed time since the test started, in seconds
     * @return a {@link Result} object containing aggregated load test metrics
     */
    public static Result calculateScenarioResults(List<ScenarioResult> scenarioResults,
        int currentVus, long elapsedTimeSec) {

        List<ResultStep> steps = scenarioResults.stream()
            .flatMap(sr -> sr.getResults().stream())
            .collect(Collectors.toList());

        List<Long> durations = steps.stream()
            .map(ResultStep::getDurationMs)
            .sorted()
            .collect(Collectors.toList());

        long totalScenario = scenarioResults.size();
        long totalStep = steps.size();
        long success = steps.stream().filter(ResultStep::getIsRequestSuccess).count();
        long fail = totalStep - success;
        long sum = durations.stream().mapToLong(Long::longValue).sum();
        long avg = durations.isEmpty() ? 0 : sum / durations.size();
        long min = durations.stream()
            .filter(d -> d > 0).min(Long::compareTo)
            .orElse(0L);
        long max = durations.isEmpty() ? 0 : durations.get(durations.size() - 1);
        List<Long> filtered = durations.stream()
            .filter(d -> d >= min && d <= max)
            .collect(Collectors.toList());
        long median = filtered.isEmpty() ? 0 : filtered.get(filtered.size() / 2);
        long p90 = percentile(durations, 90);
        long p95 = percentile(durations, 95);

        double failRate = totalStep == 0 ? 0 : (double) fail / totalStep;

        Iterations iterations = new Iterations.Builder()
            .rate(elapsedTimeSec == 0 ? 0 : (double) totalScenario / elapsedTimeSec)
            .count(totalScenario)
            .build();
        HttpReqs httpReqs = new HttpReqs.Builder()
            .rate(elapsedTimeSec == 0 ? 0 : (double) totalStep / elapsedTimeSec)
            .count(totalStep)
            .build();
        HttpReqDuration httpReqDuration = new HttpReqDuration.Builder()
            .avg(avg)
            .min(min)
            .med(median)
            .max(max)
            .p90(p90)
            .p95(p95)
            .build();
        HttpReqFailed httpReqFailed = new HttpReqFailed.Builder()
            .rate(failRate)
            .count(totalStep)
            .fail(fail)
            .build();

        return new Result.Builder()
            .iterations(iterations)
            .httpReqs(httpReqs)
            .httpReqDuration(httpReqDuration)
            .httpReqFailed(httpReqFailed)
            .vus(currentVus)
            .build();
    }

    /**
     * Calculates detailed metrics for a specific endpoint based on its request steps.
     *
     * <p>
     * This method is typically used for endpoint-level statistics generation. It computes the same
     * metrics as {@link #calculateScenarioResults}, but for a specific list of
     * {@link ResultStep}s.
     * </p>
     *
     * @param resultSteps    the list of request steps for a specific endpoint
     * @param currentVus     the number of virtual users at the time of calculation
     * @param elapsedTimeSec total elapsed time since the test started, in seconds
     * @return an {@link EndpointResult} object containing aggregated metrics for the endpoint
     */
    public static EndpointResult calculateResultSteps(List<ResultStep> resultSteps, int currentVus,
        long elapsedTimeSec) {
        long total = resultSteps.size();
        long success = resultSteps.stream().filter(ResultStep::getIsRequestSuccess).count();
        long fail = total - success;

        List<Long> durations = resultSteps.stream()
            .map(ResultStep::getDurationMs)
            .sorted()
            .collect(Collectors.toList());

        long sum = durations.stream().mapToLong(Long::longValue).sum();
        long avg = durations.isEmpty() ? 0 : sum / durations.size();
        long min = durations.stream()
            .filter(d -> d > 0).min(Long::compareTo)
            .orElse(0L);
        long max = durations.isEmpty() ? 0 : durations.get(durations.size() - 1);
        List<Long> filtered = durations.stream()
            .filter(d -> d >= min && d <= max)
            .collect(Collectors.toList());
        long median = filtered.isEmpty() ? 0 : filtered.get(filtered.size() / 2);
        long p90 = percentile(durations, 90);
        long p95 = percentile(durations, 95);

        double failRate = total == 0 ? 0 : (double) fail / total;

        Iterations iterations = new Iterations.Builder()
            .rate((double) total / elapsedTimeSec)
            .count(total)
            .build();
        HttpReqs httpReqs = new HttpReqs.Builder()
            .rate((double) total / elapsedTimeSec)
            .count(total)
            .build();
        HttpReqDuration httpReqDuration = new HttpReqDuration.Builder()
            .avg(avg)
            .min(min)
            .med(median)
            .max(max)
            .p90(p90)
            .p95(p95)
            .build();
        HttpReqFailed httpReqFailed = new HttpReqFailed.Builder()
            .rate(failRate)
            .count(total)
            .fail(fail)
            .build();

        return new EndpointResult.Builder()
            .httpReqs(httpReqs)
            .httpReqDuration(httpReqDuration)
            .httpReqFailed(httpReqFailed)
            .vus(currentVus)
            .build();
    }

    /**
     * Calculates the Nth percentile value from a sorted list of durations.
     *
     * @param sortedDurations a list of durations sorted in ascending order
     * @param percentile      the percentile to compute (e.g., 90, 95)
     * @return the latency value at the specified percentile, or 0 if the list is empty
     */
    private static long percentile(List<Long> sortedDurations, int percentile) {
        if (sortedDurations.isEmpty()) {
            return 0;
        }
        int index = (int) Math.ceil(percentile / 100.0 * sortedDurations.size()) - 1;
        return sortedDurations.get(Math.min(index, sortedDurations.size() - 1));
    }
}