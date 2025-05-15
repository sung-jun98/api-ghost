package com.apighost.loadtest.publisher;

import com.apighost.model.loadtest.result.LoadTestSnapshot;
import com.apighost.model.loadtest.result.LoadTestSummary;
import java.util.Arrays;
import java.util.List;

/**
 * A {@code ResultPublisher} implementation that delegates to multiple other publishers.
 * <p>
 * This allows combining multiple result handlers (e.g., CLI + SSE + File logging) without modifying
 * core load test execution logic. Each {@code publish} and {@code complete} call is forwarded to
 * all contained delegates in the order they were provided.
 *
 * <p>Typical use:
 * <pre>{@code
 * ResultPublisher publisher = new CompositeResultPublisher(
 *     new BufferedCliPublisher(),
 *     new SSEPublisher()
 * );
 * }</pre>
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class CompositeResultPublisher implements ResultPublisher {

    private final List<ResultPublisher> delegates;

    /**
     * Constructs a composite publisher that delegates to the given publishers.
     *
     * @param publishers one or more {@code ResultPublisher} instances
     */
    public CompositeResultPublisher(ResultPublisher... publishers) {
        this.delegates = Arrays.asList(publishers);
    }

    /**
     * Publishes a single result to all delegate publishers.
     *
     * @param snapshot the result of a single scenario step
     */
    @Override
    public void publish(LoadTestSnapshot snapshot) {
        for (ResultPublisher resultPublisher : delegates) {
            resultPublisher.publish(snapshot);
        }
    }

    /**
     * Invokes completion on all delegate publishers with the final test summary.
     *
     * @param summary aggregated information about the completed test
     */
    @Override
    public void complete(LoadTestSummary summary) {
        for (ResultPublisher resultPublisher : delegates) {
            resultPublisher.complete(summary);
        }
    }
}
