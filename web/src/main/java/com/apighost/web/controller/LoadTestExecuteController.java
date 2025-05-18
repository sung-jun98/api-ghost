package com.apighost.web.controller;

import com.apighost.loadtest.publisher.ResultPublisher;
import com.apighost.model.loadtest.parameter.LoadTestExecuteParameter;
import com.apighost.model.loadtest.parameter.LoadTestParameter;
import com.apighost.parser.loadtest.converter.LoadTestParameterConverter;
import com.apighost.parser.loadtest.reader.LoadTestParameterReader;
import com.apighost.parser.loadtest.reader.YamlLoadTestParameterReader;
import com.apighost.parser.scenario.reader.ScenarioReader;
import com.apighost.parser.scenario.reader.YamlScenarioReader;
import com.apighost.util.file.BasePathHolder;
import com.apighost.util.file.FileType;
import com.apighost.util.file.FileUtil;
import com.apighost.web.sse.SSEPublisher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * If the user sends the LoadtestParam file name as a request, the class is conducted as a subway
 * test according to the file.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class LoadTestExecuteController implements ApiController {

    private static LoadTestParameterReader reader;
    private static LoadTestParameter loadTestParameter;
    private static LoadTestParameterConverter converter;
    private static ScenarioReader scenarioReader;

    /**
     * 1. Extract LoadtestParam in Request <br>
     * 2. Loadtest file inquiry <br>
     * 3. Convert to LoadtestParameter-> LoadtestExecuteParameter <br>
     * 4. Ssepublisher creation and connection <br>
     * 5. Loadtestorchestrator creation and start <br>
     *
     * @param request LoadTestParameter File Name
     * @param response
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        String loadTestParam = request.getParameter("loadTestParam");

        if (loadTestParam != null && (loadTestParam.endsWith(".yaml") || loadTestParam.endsWith(
            ".yml"))) {
            reader = new YamlLoadTestParameterReader();
            scenarioReader = new YamlScenarioReader();
            converter = new LoadTestParameterConverter(scenarioReader);
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
            loadTestParameter = reader.readLoadParam(
                filePath.toAbsolutePath().toString());
        }

        LoadTestExecuteParameter executeParameter = converter.convert(loadTestParameter);

        ResultPublisher ssePublisher = new SSEPublisher(response);

        LoadTestOrchestrator orchestrator = new LoadTestOrchestrator(ssePublisher);
        orchestrator.start(executeParameter);

    }
}
