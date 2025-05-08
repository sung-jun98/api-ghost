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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Scenario Results File List inquiry API
 *
 * @author sun-jun98
 * @version BETA-0.0.1
 */
public class ResultListController implements ApiController {
    private ScenarioResultReader reader;

    public ResultListController() {
        this.reader = new JsonScenarioResultReader();
    }

    /**
     * Handles HTTP GET requests to retrieve a list of scenario result files,
     * processes the files, and sends the data back as a JSON response.
     *
     * @param request
     * @param response
     * @throws ServletException if there is a servlet-specific error
     * @throws IOException if an input or output error occurs while processing the request
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        try {
            List<Map<String, Object>> resultList = new ArrayList<>();

            File targetDir = FileUtil.findDirectory(FileType.RESULT);

            /** List files with .json extensions only */
            File[] files = targetDir.listFiles((dir, name) ->
                                                   name.toLowerCase().endsWith(".json"));

            for (File file : files) {
                /** Serialized the file with parser to DTO */
                ScenarioResult scenarioResult = reader.readScenarioResult(file.getAbsolutePath());
                Map<String, Object> resultMap = new HashMap<>();

                /** In serialized content, save filename, testummary, and timestamp in variables */
                resultMap.put("fileName", scenarioResult.getName());
                resultMap.put("testSummary",scenarioResult.getIsScenarioSuccess());
                resultMap.put("timeStamp", scenarioResult.getExecutedAt());

                resultList.add(resultMap);

            }

            Map<String, List<Map<String, Object>>> result = new HashMap<>();
            result.put("resultList", resultList);

            JsonUtils.writeJsonResponse(response, result, HttpServletResponse.SC_OK);

        } catch (Exception e) {
            JsonUtils.writeErrorResponse(response, "Failed to get scenario list: " + e.getMessage(),
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
