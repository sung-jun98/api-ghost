package com.apighost.cli.server;

import com.apighost.cli.util.ConsoleOutput;
import com.apighost.web.filter.CorsFilter;
import com.apighost.web.filter.ErrorHandlingFilter;
import com.apighost.web.servlet.ApiFrontControllerServlet;
import jakarta.servlet.DispatcherType;
import java.net.URL;
import java.util.EnumSet;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
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
     * A method that runs the Jetty server. Currently, the Apighost GUI requires two APIs.
     * ``/Aphost-ui/' `/aphost /`. Therefore, after creating two WebAppContexts, register resources
     * and cors filters appropriately needed for each context.
     *
     * @throws Exception
     */
    public void start() throws Exception {

        server = new Server(port);
        /** GUI Path */
        WebAppContext uiContext = new WebAppContext();
        uiContext.setContextPath("/apighost-ui");

        ResourceFactory resourceFactory = ResourceFactory.of(uiContext);
        URL resourceUrl = getClass().getClassLoader().getResource("index.html");

        if (resourceUrl != null) {
            String baseDir = resourceUrl.toURI().toString();
            baseDir = baseDir.substring(0, baseDir.lastIndexOf("index.html"));

            Resource resource = resourceFactory.newResource(baseDir);
            uiContext.setBaseResource(resource);
        }

        if (resourceUrl == null) {
            ConsoleOutput.printErrorBold(
                "Resource base URL is null. report to ghost api admin ... ");
        }

        uiContext.setWelcomeFiles(new String[]{"index.html"});

        /** API Context Path */
        WebAppContext apiContext = new WebAppContext();
        apiContext.setThrowUnavailableOnStartupException(true);
        apiContext.setContextPath("/");
        apiContext.setClassLoader(Thread.currentThread().getContextClassLoader());
        apiContext.setParentLoaderPriority(true);

        ResourceFactory apiResourceFactory = ResourceFactory.of(apiContext);
        URL apiResourceUrl = getClass().getClassLoader().getResource("");
        if (apiResourceUrl != null) {
            Resource apiResource = apiResourceFactory.newResource(apiResourceUrl.toURI());
            apiContext.setBaseResource(apiResource);
        } else {
            ConsoleOutput.printErrorBold(
                "API resource base URL is null. Cannot start server.");
            throw new IllegalStateException("Could not find resource base for API context");
        }

        /** SERVLET injection implemented in WebAppContext in Web module & Add CORS Filters */

        FilterHolder errorHandlingFilterHolder = new FilterHolder(new ErrorHandlingFilter());
        apiContext.addFilter(errorHandlingFilterHolder, "/apighost/*", EnumSet.of(DispatcherType.REQUEST));

        FilterHolder corsFilterHolder = new FilterHolder(new CorsFilter());
        apiContext.addFilter(corsFilterHolder, "/apighost/*", EnumSet.of(DispatcherType.REQUEST));

        ServletHolder frontControllerHolder = new ServletHolder(new ApiFrontControllerServlet());
        frontControllerHolder.setAsyncSupported(true);
        apiContext.addServlet(frontControllerHolder, "/apighost/*");

        /** Register filters and servlets */
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.addHandler(apiContext);
        contexts.addHandler(uiContext);

        server.setHandler(contexts);
        server.start();
    }


    /**
     * Jetty Server Matching method
     *
     * @throws Exception
     */
    public void stop() throws Exception {
        if (server != null) {
            server.stop();
        }
    }

    /**
     * Waits for the server to terminate its execution.
     *
     * @throws InterruptedException
     */
    public void awaitTermination() throws InterruptedException {
        if (server != null) {
            server.join();
        }
    }

    /**
     * Determines if the Jetty server is currently running.
     *
     * @return true if the server is not null and is currently running; false otherwise.
     */
    public boolean isRunning() {
        return server != null && server.isRunning();
    }

}
