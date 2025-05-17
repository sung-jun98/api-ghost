package com.apighost.loadtest.metrics;

import com.apighost.model.scenario.ScenarioResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ResultBuffer {
    private final Queue<ScenarioResult> buffer = new ConcurrentLinkedQueue<>();

    public void add(ScenarioResult scenarioResult) {
        buffer.add(scenarioResult);
    }

    public List<ScenarioResult> drain() {
        List<ScenarioResult> results = new ArrayList<>();
        while (!buffer.isEmpty()) {
            results.add(buffer.poll());
        }
        return results;
    }
}
