package com.apighost.cli.server;

import com.apighost.cli.util.ConsoleOutput;
import com.apighost.web.filter.CorsFilter;
import com.apighost.web.sevlet.ApiFrontControllerServlet;
import jakarta.servlet.DispatcherType;
import java.net.URL;
import java.util.EnumSet;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.util.resource.Resource;

import org.eclipse.jetty.util.resource.ResourceFactory;

/**
 * The JettyServer class is responsible for managing an embedded Jetty server instance. This class
 * provides functionality to initialize, start, stop, and monitor the status of the server. The
 * server hosts a web application and serves static resources.
 *
 * @author sun-jun98
 * @version BETA-0.0.1
 */
public class JettyServer {

    private final int port;
    private Server server;

    public JettyServer(int port) {
        this.port = port;
    }

    /**
     * Starts the embedded Jetty server at the specified port and configures it to serve a web
     * application. The server hosts static resources and sets up a context path and welcome files.
     * If configured, the server can also load additional servlets.
     *
     * @throws Exception if an error occurs during the server's initialization, configuration, or
     *                   startup process.
     */
    public void start() throws Exception {
        server = new Server(port);

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");

        ResourceFactory resourceFactory = ResourceFactory.of(webapp);

        URL resourceUrl = getClass().getClassLoader().getResource("index.html");
        if (resourceUrl != null) {

            String baseDir = resourceUrl.toURI().toString();
            baseDir = baseDir.substring(0, baseDir.lastIndexOf("index.html"));

            Resource resource = resourceFactory.newResource(baseDir);
            webapp.setBaseResource(resource);
        }

        if (resourceUrl == null) {
            ConsoleOutput.printErrorBold("Resource base URL is null. report to ghost api admin ... ");
        }

        /**File to Show when Connecting Root URL */
        webapp.setWelcomeFiles(new String[]{"index.html"});

        /** Register filters and servlets */
        configureWebApp(webapp);

        server.setHandler(webapp);
        server.start();
    }

    private void configureWebApp(WebAppContext webapp) {
        /** Register CORS filter */
        FilterHolder corsFilterHolder = new FilterHolder(new CorsFilter());
        webapp.addFilter(corsFilterHolder, "/apighost/*", EnumSet.of(DispatcherType.REQUEST));

        /** Register front controller servlet */
        ServletHolder frontControllerHolder = new ServletHolder(new ApiFrontControllerServlet());
        frontControllerHolder.setAsyncSupported(true);
        webapp.addServlet(frontControllerHolder, "/apighost/*");
    }


    public void stop() throws Exception {
        if (server != null) {
            server.stop();
        }
    }

    public void awaitTermination() throws InterruptedException {
        if (server != null) {
            server.join();
        }
    }

    public boolean isRunning() {
        return server != null && server.isRunning();
    }

}
