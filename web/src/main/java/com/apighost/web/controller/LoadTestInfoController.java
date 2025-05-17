package com.apighost.web.controller;

import com.apighost.model.loadtest.parameter.LoadTestParameter;
import com.apighost.parser.loadtest.reader.LoadTestParameterReader;
import com.apighost.parser.loadtest.reader.YamlLoadTestParameterReader;
import com.apighost.util.file.BasePathHolder;
import com.apighost.util.file.FileType;
import com.apighost.util.file.FileUtil;
import com.apighost.web.util.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller to search the details of the specific load test configuration file
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class LoadTestInfoController implements ApiController {

    public static LoadTestParameterReader reader;

    /**
     * 1. Extract file name received as a request
     * 2. Check if the extension of the file is YAML or YML
     * 3. LoadtestParameterReader view a specific YAML file in the folder of the computer.
     * 4. Filname and LoadtestParameter are sent together and sent as a response.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String loadTestParam = request.getParameter("loadTestParam");

        if (loadTestParam != null && (loadTestParam.endsWith(".yaml") || loadTestParam.endsWith(
            ".yml"))) {
            reader = new YamlLoadTestParameterReader();
        } else {
            throw new IllegalArgumentException("It is the form of the wrong file.");
        }

        Path loadTestDirectory = FileUtil.findDirectory(FileType.LOADTEST,
            BasePathHolder.getInstance().getBasePath());

        Path filePath = loadTestDirectory.resolve(loadTestParam);

        boolean fileExists = Files.exists(filePath) && Files.isRegularFile(filePath);

        if (!fileExists) {
            throw new FileNotFoundException("There is no file.: " + loadTestParam);

        } else {
            LoadTestParameter loadTestParameter = reader.readLoadParam(
                filePath.toAbsolutePath().toString());

            Map<String, Object> result = makeCustomizedMap(loadTestParam, loadTestParameter);

            JsonUtils.writeJsonResponse(response, result, HttpServletResponse.SC_OK);

        }
    }

    /**
     * Model LoadtestParameter, which contains the settings of the subway test, as well as the
     * column called Filename. The method you created because you have to send it in the response
     *
     * @param fileName
     * @param parameter
     * @return
     */
    private Map<String, Object> makeCustomizedMap(String fileName, LoadTestParameter parameter) {
        Map<String, Object> result = new HashMap<>();
        result.put("fileName", fileName);
        result.put("name", parameter.getName());
        result.put("description", parameter.getDescription());
        result.put("thinkTimeMs", parameter.getThinkTimeMs());
        result.put("stage", parameter.getStages());
        result.put("scenarios", parameter.getScenarios());
        return result;
    }
}
