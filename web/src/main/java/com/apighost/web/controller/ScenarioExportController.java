package com.apighost.web.controller;

import com.apighost.parser.scenario.writer.ScenarioWriter;
import com.apighost.parser.scenario.writer.YamlScenarioWriter;
import com.apighost.web.util.FileType;
import com.apighost.web.util.FileUtil;
import com.apighost.web.util.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.apighost.model.scenario.Scenario;

/**
 * Handles HTTP POST requests for exporting scenario data by converting JSON input into a YAML file
 * and storing it in a specific scenario directory.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class ScenarioExportController implements ApiController {

    /**
     * Handles HTTP POST requests by processing a JSON input, converting it to a YAML file, and
     * storing it in a designated scenario directory(Under `User\.apighogst\SCENARIO` path)
     * <p>
     * This method reads the JSON payload from the request, parses it into a {@link Scenario} object
     * using {@link ObjectMapper}, converts the parsed object into YAML format with
     * {@link YamlScenarioWriter}, and persists the YAML file in a directory specified for
     * scenarios.
     *
     * @param request  the HTTP servlet request that contains the JSON payload
     * @param response the HTTP servlet response where the method writes the response message
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 1. Remove the text separately from the request */
        StringBuilder requestBody = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        try {
            /** 2. Converted a JSON -shaped request to JSON object */
            ObjectMapper jsonMapper = new ObjectMapper();
            Scenario scenario = jsonMapper.readValue(requestBody.toString(), Scenario.class);

            /** 3.Convert to YAML file through parser writer */
            ScenarioWriter writer = new YamlScenarioWriter();

            /** 4. Store the converted YAML object inside the scenario folder */
            File scenarioDir = FileUtil.findDirectory(FileType.SCENARIO);
            String fileName = "scenario_" + scenario.getName() + ".yaml";
            String filePath = new File(scenarioDir, fileName).getAbsolutePath();
            writer.writeScenario(scenario, filePath);

            /** 5. return response  */
            JsonUtils.writeJsonResponse(response,
                Map.of("message", "The scenario has been successfully stored.", "filePath",
                    filePath),
                HttpServletResponse.SC_OK);

        } catch (IOException e) {
            JsonUtils.writeErrorResponse(response,
                "An error occurred during the scenario conversion: " + e.getMessage(),
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}


