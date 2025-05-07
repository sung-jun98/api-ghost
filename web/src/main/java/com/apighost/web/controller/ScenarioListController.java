package com.apighost.web.controller;

import com.apighost.web.util.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.apighost.web.util.FileType;
import com.apighost.web.util.FileUtil;

/**
 * Scenario File List inquiry API
 *
 * @author sun-jun98
 * @version BETA-0.0.1
 */
public class ScenarioListController implements ApiController {


    /**
     * Handles HTTP GET requests to retrieve a list of scenario file names
     * from a specific directory, filters files with ".yaml" or ".yml"
     * extensions, and sends the list back as a JSON response.
     *
     * @param request
     * @param response
     * @throws ServletException if there is a servlet-specific error
     * @throws IOException if an input or output error occurs while processing
     *                     the request
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        try {
            List<String> scenarioNames = new LinkedList<>();

            File targetDir = FileUtil.findDirectory(FileType.SCENARIO);

            /** List files with .yaml or .yml extensions only */
            File[] files = targetDir.listFiles((dir, name) ->
                                                   name.toLowerCase().endsWith(".yaml") ||
                                                       name.toLowerCase().endsWith(".yml")
            );

            for (File file : files) {
                scenarioNames.add(file.getName());
            }

            Map<String, List<String>> result = new HashMap<>();
            result.put("scenarioNameList", scenarioNames);

            JsonUtils.writeJsonResponse(response, result, HttpServletResponse.SC_OK);

        } catch (Exception e) {
            JsonUtils.writeErrorResponse(response, "Failed to get scenario list: " + e.getMessage(),
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
