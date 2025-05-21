package com.apighost.cli.util.console;

import com.apighost.cli.util.ConsoleOutput;
import com.apighost.model.loadtest.result.Endpoint;
import com.apighost.model.loadtest.result.LoadTestSnapshot;
import com.apighost.model.loadtest.result.LoadTestSummary;
import com.apighost.model.loadtest.result.Result;
import com.apighost.model.loadtest.result.metric.HttpReqDuration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DynamicDisplayManager and ANSIESCAPEUTIL using a class that dynamically accumulates the results
 * in the console window.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class LoadTestReporter {

    private final static int LOGO_LINE = 12;

    private final DynamicDisplayManager display;
    private long startTime;
    private int timeStampLine = 2 + LOGO_LINE;
    private int durationLine = 3 + LOGO_LINE;
    private int progressBarLine = 4 + LOGO_LINE;
    private int httpReqLine = 5 + LOGO_LINE;
    private int httpFailLine = 6 + LOGO_LINE;
    private int vusLine = 7 + LOGO_LINE;
    private int iterationsLine = 8 + LOGO_LINE;
    private int httpReqsLine = 9 + LOGO_LINE;
    private int endpointStartLine = 11 + LOGO_LINE;
    private int previousLine = endpointStartLine;
    private Map<Integer, String> updates;

    /**
     * Define a dynamicdisplaymanager that deals with strings using printwriter in the console
     * window.
     */
    public LoadTestReporter() {
        this.display = new DynamicDisplayManager();
        startTime = System.currentTimeMillis();
        initializeLayout();
    }

    /**
     * After initializing the ANSIESCAPE -related tools to show the load test results dynamically,
     * Show and define the first basic message every 5 seconds before receiving the result of the
     * load test.
     */
    private void initializeLayout() {
        display.initialize();
        display.println("░░░░░░░░░░░░░░░▒▒▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░");
        display.println("░░░░░░░░░░░▒▓▒░░░░░░▒▓▒░░░░░░░░░░▒░░░░░▒▒▒▒░░░▒░░░░░░░░░░░░░░░░░░");
        display.println("░░░░░░░░░▒▓░░░░░░░░░░░░▓░░░░░░░░▓▓▓▒░░░▓░░░▓▓░▓▒░░░░░░░░░░░░░░░░░");
        display.println("░░░░░░░░▓▒░░░░░░░░░░░░░░▓░░░░░░▒▓░▒▓░░░▓░░░▓▓░▓▒░░░░░░░░░░░░░░░░░");
        display.println("░░░░░░░░▓░░░▒▓▓░░░▒▓▓░░░░▓░░░░░▓▓▒▒▓▒░░▓▒▒▒░░░▓▒░░░░░░░░░░░░░░░░░");
        display.println("░░░░░░░░▓░░░░▓▓░░░░▓▓░░░░▓░░░░▒▓░░░░▓░░▓░░░░░░▓▒░░░░░░░░░░░░░░░░░");
        display.println("░░░░░░░░▓░░░░░░░░░░░░░░░░▓░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░");
        display.println("░░░░░░░▒▓░░░░░▒▓▒▓▓░░░░░░▓░░░░░▒▓▓▓░░▒▓░░░░░░░░░░░░░░░░░░░░░░░░░░");
        display.println("░░░░░░░▓▒░░░░░░░░░░░░░░░░▓░░░▒▓░░░░▒░▒▓░▒▒░░░░▒▒░░░░▒▓▒░▒▓▓▒░░░░░");
        display.println("░░░░░░░▓░░░░░░░░░░░░░░░░░▓░░░▓▒░░▒▓▓▒▒▓░░▒▓░▓▒░░▒▓░▓▒░░░░▒▓░░░░░░");
        display.println("░░░░░░▓░░░░░░░░░░░░░░░░░░▓░░░▒▓░░░░▓▒▒▓░░░▓░▓▒░░▒▓░░░▒▓▓░▒▓░░░░░░");
        display.println("░░░░░▒▓░░▒▓▓▓▓▒░░░▓▓▓▓▒░▓▒░░░░░▓▓▓▓▒░▒▓░░░▓░░▓▓▓▓░░▓▓▓▓▒░▒▓▓░░░░░");
        display.println("API Ghost Under load test");

        String bar = getProgressBar(0, 20);
        display.println("  timestamp.....................: Initialization...");
        display.println("  duration......................: 0s");
        display.println("  progress.......................: " + bar + " 0s / 60s");
        display.println(
            "  http_req_duration..............: avg=0ms      min=0ms      med=0ms     max=0ms    p(90)=0ms    p(95)=0ms");
        display.println("  http_req_failed................: 0.00%  O 0        X 0");
        display.println("  vus...........................: 0");
        display.println("  Number of scenarios performed Now...... : 0");
        display.println("  Number of HttpRequests Now..............: 0");
        display.println(" ");
    }

    /**
     * A method that changes Loadtestsnapshot to the terminal console window every 5 seconds
     *
     * @param snapshot Loadtest EXECUTER is a snapshot object that sends every 5 seconds.
     */
    public void updateSnapshot(LoadTestSnapshot snapshot) {
        Result result = snapshot.getResult();
        updates = new HashMap<>();

        updates.put(timeStampLine,
            "  timestamp.....................: " + snapshot.getTimeStamp());

        long elapsedSecs = (System.currentTimeMillis() - startTime) / 1000;
        updates.put(durationLine,
            "  duration......................: " + formatDuration(elapsedSecs));

        double progressRatio = (elapsedSecs * 1.0) / 60;
        progressRatio = Math.min(progressRatio, 1.0);
        updates.put(progressBarLine,
            "  progress.......................: " + getProgressBar(progressRatio, 20));

        HttpReqDuration duration = result.getHttpReqDuration();
        updates.put(httpReqLine, String.format(
            "  http_req_duration..............: avg=%-8s min=%-8s med=%-8s max=%-8s p(90)=%-8s p(95)=%-8s",
            formatMs(duration.getAvg()),
            formatMs(duration.getMin()),
            formatMs(duration.getMed()),
            formatMs(duration.getMax()),
            formatMs(duration.getP90()),
            formatMs(duration.getP95())
        ));

        long totalRequests = result.getHttpReqs().getCount();
        long failedRequests = result.getHttpReqFailed().getFail();
        double failureRate = failedRequests * 100.0 / (totalRequests > 0 ? totalRequests : 1);
        failureRate = Math.ceil(failureRate * 100) / 100;
        updates.put(httpFailLine, String.format(
            "  http_req_failed................: %.2f%%  O %d        X %d",
            failureRate, totalRequests - failedRequests, failedRequests
        ));

        updates.put(vusLine,
            String.format("  vus...........................: %d", result.getVus()));

        updates.put(iterationsLine,
            String.format("  Number of scenarios performed Now...... : %.2f",
                result.getIterations().getRate()));

        updates.put(httpReqsLine,
            String.format("  Number of HttpRequests Now..............: %.2f",
                result.getHttpReqs().getRate()));

        updateEndpointInfo(snapshot.getEndpoints());
        display.updateScreen(updates);
    }

    /**
     * A method that changes LoadTestSummary to the terminal console window at the end
     *
     * @param summary the final response of LoadTest
     */
    public void updateSummary(LoadTestSummary summary) {
        Result result = summary.getResult();
        updates = new HashMap<>();

        updates.put(timeStampLine,
            "  startTime ......." + summary.getStartTime() + ".......EndTime......."
                + summary.getEndTime());

        long elapsedSecs = (System.currentTimeMillis() - startTime) / 1000;
        updates.put(durationLine,
            "  duration......................: " + formatDuration(elapsedSecs));

        HttpReqDuration duration = result.getHttpReqDuration();
        updates.put(httpReqLine, String.format(
            "  http_req_duration..............: avg=%-8s min=%-8s med=%-8s max=%-8s p(90)=%-8s p(95)=%-8s",
            formatMs(duration.getAvg()),
            formatMs(duration.getMin()),
            formatMs(duration.getMed()),
            formatMs(duration.getMax()),
            formatMs(duration.getP90()),
            formatMs(duration.getP95())
        ));

        long totalRequests = result.getHttpReqs().getCount();
        long failedRequests = result.getHttpReqFailed().getFail();
        double failureRate = failedRequests * 100.0 / (totalRequests > 0 ? totalRequests : 1);
        failureRate = Math.ceil(failureRate * 100) / 100;
        updates.put(httpFailLine, String.format(
            "  http_req_failed................: %.2f%%  O %d        X %d",
            failureRate, totalRequests - failedRequests, failedRequests
        ));

        updates.put(vusLine,
            String.format("  vus...........................: %d", result.getVus()));

        updates.put(iterationsLine,
            String.format("  Total scenarios performed ............ : %d",
                result.getIterations().getCount()));

        updates.put(httpReqsLine,
            String.format("  Total HttpRequests ..................... : %d",
                result.getHttpReqs().getCount()));
        updateEndpointInfo(summary.getEndpoints());
        display.updateScreen(updates);
    }

    /**
     * The amount of Endpoint is different for each scenario test. Therefore, the method was taken
     * out separately so that it was output by the length.
     *
     * @param endpoints
     */
    private void updateEndpointInfo(List<Endpoint> endpoints) {
        int line = endpointStartLine;
        updates = updates != null ? updates : new HashMap<>();

        for (Endpoint endpoint : endpoints) {
            HttpReqDuration duration = endpoint.getResult().getHttpReqDuration();
            String content = String.format(
                "  %-35s: avg=%-8s min=%-8s med=%-8s max=%-8s",
                "{ endpoint:" + endpoint.getUrl() + " }",
                formatMs(duration.getAvg()),
                formatMs(duration.getMin()),
                formatMs(duration.getMed()),
                formatMs(duration.getMax())
            );
            updates.put(line, content);
            line++;
        }

        for (int i = previousLine; i < line; i++) {
            display.println(" ");
        }
        for (int i = line; i < previousLine; i++) {
            updates.put(i, "");
        }
        previousLine = line;
    }

    /**
     * Data is converted to Second because the Long data type is expressed in Millisecond.
     *
     * @param seconds
     * @return fomatted duration
     */
    private String formatDuration(long seconds) {
        if (seconds < 60) {
            return seconds + "s";
        }
        return String.format("%dm%ds", seconds / 60, seconds % 60);
    }

    /**
     * Printwriter's print methods change the long Millisecond to String because only the String
     * data type is received as a parameter.
     *
     * @param ms
     * @return formatted second as a String
     */
    private String formatMs(long ms) {
        return ms + "ms";
    }

    private String getProgressBar(double ratio, int width) {
        int completed = (int) (ratio * width);
        int remaining = width - completed;

        return "[" +
            "█".repeat(completed) +
            "-".repeat(remaining) +
            "] " + (int) (ratio * 100) + "%";
    }
}