package com.apighost.web.controller;

import com.apighost.model.collector.Endpoint;
import com.apighost.web.collector.ApiCollector;
import com.apighost.web.util.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller responsible for handling HTTP GET requests to retrieve available API endpoints
 * collected from scenario configuration files.
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class EndpointController implements ApiController {

    /**
     * Utility that collects endpoints from scenario definitions.
     */
    private final ApiCollector apiCollector;

    /**
     * Constructs a new {@code EndpointController} with a default {@link ApiCollector} instance.
     */
    public EndpointController() {
        this.apiCollector = new ApiCollector();
    }

    /**
     * Handles HTTP GET requests to return a list of collected endpoints as JSON.
     *
     * @param request  the {@link HttpServletRequest} object
     * @param response the {@link HttpServletResponse} object
     * @throws ServletException in case of a servlet error
     * @throws IOException      in case of I/O error during response writing
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        try {
            List<Endpoint> endpointList = apiCollector.getEndPointList();
            JsonUtils.writeJsonResponse(response, endpointList,
                HttpServletResponse.SC_OK);
        } catch (Exception e) {
            JsonUtils.writeErrorResponse(response,
                "Failed to get endpoint: " + e.getMessage(),
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
