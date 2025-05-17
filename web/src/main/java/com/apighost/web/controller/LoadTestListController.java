package com.apighost.web.controller;

import com.apighost.util.file.BasePathHolder;
import com.apighost.util.file.FileUtil;
import com.apighost.util.file.FileType;
import com.apighost.web.util.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A controller that returns a list of configuration files inside the folder`.apighost/LOADTEST`
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class LoadTestListController implements ApiController {

    /**
     * 1. check `.apighost/LOADTEST` directory whether there's valid loadtest parameter files or not
     * 2. load on the list to send as a response
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        List<Map<String, Object>> loadTestParamNameList = new ArrayList<>();

        Path loadTestDirectory = FileUtil.findDirectory(FileType.LOADTEST,
            BasePathHolder.getInstance().getBasePath());

        long fileCount = Files.list(loadTestDirectory).count();

        if (fileCount == 0) {
            JsonUtils.writeErrorResponse(response, "empty directory ",
                HttpServletResponse.SC_NOT_FOUND);

            return;
        }

        try {
            Files.walk(loadTestDirectory)
                .filter(path -> Files.isRegularFile(path))
                .forEach(path -> {
                    Map<String, Object> fileInfo = new HashMap<>();
                    fileInfo.put("fileName", path.getFileName().toString());
                    loadTestParamNameList.add(fileInfo);
                });

            Map<String, List<Map<String, Object>>> result = new HashMap<>();
            result.put("loadTestParamNameList", loadTestParamNameList);

            JsonUtils.writeJsonResponse(response, result, HttpServletResponse.SC_OK);

        } catch (IOException e) {
            JsonUtils.writeErrorResponse(response,
                "Failed to get load test list: " + e.getMessage(),
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
