package com.apighost.web.controller;

import com.apighost.model.scenario.ScenarioResult;
import com.apighost.parser.scenario.reader.JsonScenarioResultReader;
import com.apighost.parser.scenario.reader.ScenarioResultReader;
import com.apighost.web.util.FileType;
import com.apighost.web.util.FileUtil;
import com.apighost.web.util.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * This class is responsible for handling HTTP GET requests related to fetching scenario results.
 *
 * @author sun-jun98
 * @version BETA-0.0.1
 */
public class ResultInfoController implements ApiController {

    private ScenarioResultReader reader;

    public ResultInfoController() {
        this.reader = new JsonScenarioResultReader();
    }

    /**
     * Handles HTTP GET requests to fetch scenario result details.
     * <p>
     * This method processes a request by searching for a scenario result file within a predefined
     * results directory. If a matching file is found, it deserializes the content into a
     * ScenarioResult object and responds with its details in JSON format.
     * If no matching result is found or an error occurs during processing, it sends an appropriate error response.
     * </p>
     *
     * @param request  Expects a "testResultName" parameter to specify the name of the desired
     *                 scenario result.
     * @param response HTTP response sent back to the client. Contains either the matching scenario
     *                 result in JSON format or an error response in case of failure or no match
     *                 found.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String testResultName = request.getParameter("testResultName");

        try {
            File targetDir = FileUtil.findDirectory(FileType.RESULT);

            File[] files = targetDir.listFiles((dir, name) ->
                                                   name.toLowerCase().endsWith(".json"));

            for (File file : files) {
                String searchedName = file.getName();

                if (searchedName.equals(testResultName)) {
                    ScenarioResult scenarioResult = reader.readScenarioResult(
                        file.getAbsolutePath());
                    JsonUtils.writeJsonResponse(response, scenarioResult,
                        HttpServletResponse.SC_OK);
                }
            }

            JsonUtils.writeErrorResponse(response, "No matching scenario result found.",
                HttpServletResponse.SC_NOT_FOUND);

        } catch (Exception e) {
            JsonUtils.writeErrorResponse(response,
                "Failed to get scenario result detail: " + e.getMessage(),
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
