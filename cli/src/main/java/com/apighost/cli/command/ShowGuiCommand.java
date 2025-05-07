package com.apighost.cli.command;

import com.apighost.cli.server.JettyServer;
import com.apighost.cli.util.ConsoleOutput;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * If you want to make a drag and drop in the GUI, rather than the console command in the CLI, the
 * GUI entry command JetTy's embedded mode floats index.html inside the server in 7000.
 *
 * <p>
 * Example Usage : `apighost ui` or `apighost ui -p 7001` or `apighost ui --port 7001`
 * </p>
 *
 * @author sun-jun98
 * @version BETA-0.0.1
 */
@Command(
    name = "ui",
    description = "Start the API Ghost web canvas interface",
    mixinStandardHelpOptions = true
)
public class ShowGuiCommand implements Callable<Integer> {

    @Option(names = {"-p",
        "--port"}, description = "Port to start the GUI on", defaultValue = "7000")
    private int port;

    /**
     * A command to run the Canvas UI in GUI mode.
     * Run the web server in the specified port and automatically open the browser.
     *
     * @return an Integer indicating the result of the operation: 0 if the server started and
     * terminated successfully, 1 in case of an error during the startup process.
     * @throws Exception if an exception occurs while starting, running, or terminating the server.
     */
    @Override
    public Integer call() throws Exception {
        ConsoleOutput.print("Starting API Ghost canvas on port " + port + "...");

        /** run the web server */
        JettyServer server = new JettyServer(port);
        try {
            server.start();
            openBrowser("http://localhost:" + port);

            ConsoleOutput.print("API Ghost canvas is running at http://localhost:" + port);
            ConsoleOutput.printBold(
                "Visit http://localhost:" + port + "/canvas to see the Canvas UI");
            ConsoleOutput.print("Press Ctrl+C to stop the server...");

            /** wait until webserver is end */
            server.awaitTermination();
            return 0;
        } catch (Exception e) {
            ConsoleOutput.printError("Error starting server: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * Open the URL in the default system browser.
     *
     * @param url URL to open
     */
    private void openBrowser(String url) {
        try {
            /** Different ways to run browser by platform */
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                /** Windows */
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (os.contains("mac")) {
                /** macOS */
                Runtime.getRuntime().exec("open " + url);
            } else if (os.contains("nix") || os.contains("nux")) {
                /** Linux */
                Runtime.getRuntime().exec("xdg-open " + url);
            }
        } catch (Exception e) {
            System.err.println("Could not open browser: " + e.getMessage());
        }
    }
}
