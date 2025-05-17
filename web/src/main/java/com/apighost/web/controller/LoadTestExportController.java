package com.apighost.web.controller;

import com.apighost.model.loadtest.parameter.LoadTestParameter;
import com.apighost.parser.loadtest.writer.LoadTestParameterWriter;
import com.apighost.parser.loadtest.writer.YamlLoadTestParameterWriter;
import com.apighost.util.file.BasePathHolder;
import com.apighost.util.file.FileType;
import com.apighost.util.file.FileUtil;
import com.apighost.web.util.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * When you receive the contents of the load test, the controller that stores the file inside the .apighost/Roadtest folder.
 * Throw {"status": true} as a response value.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class LoadTestExportController implements ApiController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private LoadTestParameter loadTestParameter;
    private LoadTestParameterWriter writer;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        Map<String, Object> requestBody = objectMapper.readValue(request.getReader(),
            Map.class);
        /**
         * Filename extraction at request
         */
        String fileName = (String) requestBody.get("fileName");

        if (fileName == null || !fileName.matches(".*\\.(yaml|yml)$")) {
            JsonUtils.writeErrorResponse(response, "invalid file name.",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        /**
         *  Convert the remaining elements to LoadtestParam
         */
        try {
            requestBody.remove("fileName"); // fileName 제거 후 변환
            loadTestParameter = objectMapper.convertValue(requestBody, LoadTestParameter.class);
        } catch (Exception e) {
            JsonUtils.writeErrorResponse(response,
                "the file content structure is not supported: " + e.getMessage(),
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Path loadTestDirectory = FileUtil.findDirectory(FileType.LOADTEST,
            BasePathHolder.getInstance().getBasePath());
        Path filePath = loadTestDirectory.resolve(fileName);

        /**
         *  Newly created when there is no directory
         */
        if (!Files.exists(loadTestDirectory)) {
            Files.createDirectories(loadTestDirectory);
        }

        /**
         * When you end with the YAML file, write it with the YAML file.
         * If the file already exists, cover it.
         * If there is no file, make a new one.
         */
        if (fileName.endsWith(".yaml") || fileName.endsWith(".yml")) {
            writer = new YamlLoadTestParameterWriter();
            writer.writeLoadParam(loadTestParameter, filePath.toAbsolutePath().toString());

            JsonUtils.writeJsonResponse(response,
                Map.of("status", true),
                HttpServletResponse.SC_OK);
        } else {
            JsonUtils.writeErrorResponse(response, "It is the form of the wrong file.",
                HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
