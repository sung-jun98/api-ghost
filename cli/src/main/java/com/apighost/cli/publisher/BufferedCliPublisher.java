package com.apighost.cli.publisher;

import com.apighost.cli.util.console.LoadTestReporter;
import com.apighost.loadtest.publisher.ResultPublisher;
import com.apighost.model.loadtest.result.LoadTestSnapshot;
import com.apighost.model.loadtest.result.LoadTestSummary;

/**
 * Class to be used when the Loadtest -related Scheduler empty the buffer and output to the user's
 * console window.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class BufferedCliPublisher implements ResultPublisher {

    LoadTestReporter reporter;

    public BufferedCliPublisher() {
        this.reporter = new LoadTestReporter();
    }

    @Override
    public void publish(LoadTestSnapshot snapshot) {
        reporter.updateSnapshot(snapshot);
    }

    /**
     * Invokes completion on all delegate publishers with the final test summary.
     *
     * @param summary aggregated information about the completed test
     */
    @Override
    public void complete(LoadTestSummary summary) {
        reporter.updateSummary(summary);
    }
}
