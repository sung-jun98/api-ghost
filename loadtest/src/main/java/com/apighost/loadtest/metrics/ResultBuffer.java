package com.apighost.loadtest.metrics;

import com.apighost.model.scenario.ScenarioResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Thread-safe buffer for temporarily storing {@link ScenarioResult} instances during load test
 * execution.
 *
 * <p>
 * This class is designed for concurrent environments and allows multiple threads to add results
 * safely. Stored results can be drained all at once for aggregation or reporting.
 * </p>
 *
 * <p>
 * Typical usage involves repeatedly calling {@link #add(ScenarioResult)} from parallel workers and
 * periodically calling {@link #drain()} from a scheduler or reporter thread.
 * </p>
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class ResultBuffer {

    /**
     * Internal queue used to buffer results in a thread-safe manner.
     */
    private final Queue<ScenarioResult> buffer = new ConcurrentLinkedQueue<>();

    /**
     * Adds a {@link ScenarioResult} to the buffer.
     *
     * @param scenarioResult the scenario result to add
     */
    public void add(ScenarioResult scenarioResult) {
        buffer.add(scenarioResult);
    }

    /**
     * Drains all buffered {@link ScenarioResult} instances into a list.
     *
     * <p>
     * This method removes and returns all elements currently in the buffer.
     * </p>
     *
     * @return a list containing all drained scenario results, or an empty list if the buffer was
     * empty
     */
    public List<ScenarioResult> drain() {
        List<ScenarioResult> results = new ArrayList<>();
        while (!buffer.isEmpty()) {
            results.add(buffer.poll());
        }
        return results;
    }
}
