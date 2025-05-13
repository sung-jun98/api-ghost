package com.apighost.web.controller;

import com.apighost.model.scenario.Scenario;
import com.apighost.parser.scenario.reader.ScenarioReader;
import com.apighost.parser.scenario.reader.YamlScenarioReader;
import com.apighost.web.util.FileType;
import com.apighost.web.util.FileUtil;
import com.apighost.web.util.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller uses a {@link ScenarioReader} implementation (in this case,
 * {@link YamlScenarioReader}) to read scenario details from files with `.yaml` or `.yml` extensions
 * in a predefined directory.
 *
 * @author sun-jun98
 * @version BETA-0.0.1
 */
public class ScenarioInfoController implements ApiController {

    private ScenarioReader reader;

    public ScenarioInfoController() {
        this.reader = new YamlScenarioReader();
    }

    /**
     * Handles HTTP GET requests to retrieve a scenario by its name. The method looks for files
     * with `.yaml` or `.yml` extensions in a predefined directory, matches the specified scenario name,
     * and returns the scenario details in JSON format. If no matching scenario is found, it returns an
     * error response.
     *
     * @param request  the HttpServletRequest object containing the request data, including the
     *                 "scenarioName" parameter
     * @param response the HttpServletResponse object used to send back the JSON response to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String scenarioName = request.getParameter("scenarioName");

        try {
            List<String> scenarioNames = new ArrayList<>();

            File targetDir = FileUtil.findDirectory(FileType.SCENARIO);

            /** List files with .yaml or .yml extensions only */
            File[] files = targetDir.listFiles((dir, name) ->
                                                   name.toLowerCase().endsWith(".yaml") ||
                                                       name.toLowerCase().endsWith(".yml")
            );

            for (File file : files) {
                String searchedName = file.getName();

                if (searchedName.equals(scenarioName)) {
                    Scenario scenario = reader.readScenario(file.getAbsolutePath());
                    JsonUtils.writeJsonResponse(response, scenario, HttpServletResponse.SC_OK);
                }
            }

            /** Return an error without the file found*/
            JsonUtils.writeErrorResponse(response, "No matching scenario found.",
                HttpServletResponse.SC_NOT_FOUND);

        } catch (Exception e) {
            JsonUtils.writeErrorResponse(response, "Failed to get scenario list: " + e.getMessage(),
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
