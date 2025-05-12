package com.apighost.web.controller;

import com.apighost.model.collector.Endpoint;
import com.apighost.web.collector.ApiCollector;
import com.apighost.web.util.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class EndpointController implements ApiController {

    private final ApiCollector apiCollector;

    public EndpointController() {
        this.apiCollector = new ApiCollector();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        System.out.println("Hello!");
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
