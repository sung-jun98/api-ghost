package com.apighost.cli.util.console;

import com.apighost.model.loadtest.result.Endpoint;
import com.apighost.model.loadtest.result.LoadTestSnapshot;
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

    private final DynamicDisplayManager display;
    private long startTime;
    private int timeStampLine = 2;
    private int durationLine = 3;
    private int httpReqLine = 4;
    private int endpointStartLine = 5;
    private int httpFailLine = 7;
    private int progressLine = 10;
    private Map<String, Integer> endpointLines = new HashMap<>();

    /**
     * Define a dynamicdisplaymanager that deals with strings using printwriter in the console
     * window.
     */
    public LoadTestReporter() {
        this.display = new DynamicDisplayManager();
        initializeLayout();
    }

    private void initializeLayout() {

        display.println("API Ghost Under load test");
        display.println("  timestamp.....................: Initialization...");
        display.println("  duration......................: 0s");
        display.println(
            "  http_req_duration..............: avg=0ms      min=0ms      med=0ms     max=0ms");
        display.println("  http_req_failed................: 0.00%  ✓ 0        ✗ 0");
        display.println("");

    }

    /**
     * A method that changes Loadtestsnapshot to the terminal console window every 5 seconds
     *
     * @param snapshot Loadtest EXECUTER is a snapshot object that sends every 5 seconds.
     */
    public void updateSnapshot(LoadTestSnapshot snapshot) {
        Result result = snapshot.getResult();

        display.updateLine(timeStampLine,
            "  timestamp.....................: " + snapshot.getTimeStamp());

        long elapsedSecs = (System.currentTimeMillis() - startTime) / 1000;
        display.updateLine(durationLine,
            "  duration......................: " + formatDuration(elapsedSecs));

        HttpReqDuration duration = result.getHttpReqDuration();
        display.updateLine(httpReqLine, String.format(
            "  http_req_duration..............: avg=%-8s min=%-8s med=%-8s max=%-8s",
            formatMs(duration.getAvg()),
            formatMs(duration.getMin()),
            formatMs(duration.getMed()),
            formatMs(duration.getMax())
        ));

        updateEndpointInfo(snapshot.getEndpoints());

        long totalRequests = result.getHttpReqs().getCount();
        long failedRequests = result.getHttpReqFailed().getFail();
        double failureRate = failedRequests * 100.0 / (totalRequests > 0 ? totalRequests : 1);
        display.updateLine(httpFailLine, String.format(
            "  http_req_failed................: %.2f%%  ✓ %d        ✗ %d",
            failureRate, failedRequests, totalRequests - failedRequests
        ));

    }

    /**
     * The amount of Endpoint is different for each scenario test. Therefore, the method was taken
     * out separately so that it was output by the length.
     *
     * @param endpoints
     */
    private void updateEndpointInfo(List<Endpoint> endpoints) {
        int line = endpointStartLine;

        for (Endpoint endpoint : endpoints) {
            String url = endpoint.getUrl();

            if (!endpointLines.containsKey(url)) {
                endpointLines.put(url, line);
                httpFailLine++;
                progressLine++;
            }

            line = endpointLines.get(url);
            HttpReqDuration duration = endpoint.getResult().getHttpReqDuration();

            String padding = "    { endpoint:" + url + " }";
            padding = String.format("%-35s", padding);

            display.updateLine(line, String.format(
                "  %s: avg=%-8s min=%-8s med=%-8s max=%-8s",
                padding,
                formatMs(duration.getAvg()),
                formatMs(duration.getMin()),
                formatMs(duration.getMed()),
                formatMs(duration.getMax())
            ));

            line++;
        }
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

}