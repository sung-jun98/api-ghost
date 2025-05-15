package com.apighost.loadtest.publisher;

import com.apighost.model.loadtest.result.LoadTestSnapshot;
import com.apighost.model.loadtest.result.LoadTestSummary;

/**
 * Defines the contract for publishing load test results.
 * <p>
 * A {@code ResultPublisher} receives individual test results as they are produced during the
 * execution of a load test, and a summary notification once the test is complete.
 * <p>
 * Typical implementations include CLI output, SSE broadcasting, file logging, or external system
 * integration (e.g., Kafka, DB).
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public interface ResultPublisher {

    /**
     * Publishes a single result produced during the load test. This method is called repeatedly as
     * each test step completes.
     *
     * @param snapshot the result of a single scenario step
     */
    void publish(LoadTestSnapshot snapshot);

    /**
     * Called once after all load test execution is complete. Used to publish a summary or finalize
     * output.
     *
     * @param summary aggregated information about the completed test
     */
    void complete(LoadTestSummary summary);
}
