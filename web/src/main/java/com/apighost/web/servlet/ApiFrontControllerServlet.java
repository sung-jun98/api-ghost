package com.apighost.web.servlet;

import com.apighost.web.controller.ApiController;
import com.apighost.web.controller.EndpointController;
import com.apighost.web.controller.FileRemoveController;
import com.apighost.web.controller.GenerateDataController;
import com.apighost.web.controller.LoadTestExecuteController;
import com.apighost.web.controller.LoadTestExportController;
import com.apighost.web.controller.LoadTestInfoController;
import com.apighost.web.controller.LoadTestListController;
import com.apighost.web.controller.ResultInfoController;
import com.apighost.web.controller.ResultListController;
import com.apighost.web.controller.ScenarioExportController;
import com.apighost.web.controller.ScenarioInfoController;
import com.apighost.web.controller.ScenarioListController;
import com.apighost.web.controller.ScenarioTestController;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class acts as a centralized entry point for handling API requests and routing them to the
 * appropriate controller based on the URL path.
 * <p>
 * It is designed to handle requests for paths starting with "/apighost/" and delegate the
 * processing of requests to controllers that implement the ApiController interface.
 *
 * @author sun-jun98
 * @version BETA-0.0.1
 */
public class ApiFrontControllerServlet extends HttpServlet {

    private final Map<String, ApiController> controllerMap = new HashMap<>();

    @Override
    public void init() throws ServletException {
        /** Controller registration */
        controllerMap.put("scenario-list", new ScenarioListController());
        controllerMap.put("result-list", new ResultListController());
        controllerMap.put("scenario-info", new ScenarioInfoController());
        controllerMap.put("result-info", new ResultInfoController());
        controllerMap.put("endpoint-json", new EndpointController());
        controllerMap.put("scenario-test", new ScenarioTestController());
        controllerMap.put("scenario-export", new ScenarioExportController());

        controllerMap.put("loadtest-list", new LoadTestListController());
        controllerMap.put("loadtest-export", new LoadTestExportController());
        controllerMap.put("loadtest-info", new LoadTestInfoController());
        controllerMap.put("loadtest-execute", new LoadTestExecuteController());

        controllerMap.put("file-remove", new FileRemoveController());

        controllerMap.put("generate-data", new GenerateDataController());

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** URL path analysis */
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestURI.substring(contextPath.length());

        /** only handle /apighost/xxx URL */
        if (!path.startsWith("/apighost/")) {
            throw new IllegalArgumentException("Invalid URL path: " + path);
        }

        /** Controller key extraction (/apighost/scenario-list -> scenario-list) */
        String controllerKey = path.substring("/apighost/".length());

        /** Controller inquiry */
        ApiController controller = controllerMap.get(controllerKey);
        if (controller == null) {
            throw new IllegalArgumentException("Controller not found for path: " + controllerKey);
        }

        /** Request processing */
        try {
            switch (request.getMethod()) {
                case "GET":
                    controller.doGet(request, response);
                    break;
                case "POST":
                    controller.doPost(request, response);
                    break;
                case "PUT":
                    controller.doPut(request, response);
                    break;
                case "DELETE":
                    controller.doDelete(request, response);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        } catch (Exception e) {
            throw new RuntimeException("Server error: " + e.getMessage());
        }
    }

}