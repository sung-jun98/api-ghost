package com.apighost.web.controller;

import com.apighost.util.file.BasePathHolder;
import com.apighost.util.file.FileType;
import com.apighost.util.file.FileUtil;
import com.apighost.web.util.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Controller to delete files from your computer
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class FileRemoveController implements ApiController {

    private String fileName;
    private boolean fileExists;
    private Path filePath;
    private Path loadTestDirectory;

    /**
     * 1. It gets the value of filename from the request.
     * 2. 'User/.apighost' The files are found in the lower 'Scenario', 'Result', and 'Loadtest' folders.
     * 3. If you find a file, delete the file.
     *
     * @param request It brings the value of the key called Filename.
     * @param response {SUCCESS: True}, {SUCCESS: FALSE} if failed
     * @throws IOException
     */
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        String fileName = request.getParameter("fileName");

        loadTestDirectory = FileUtil.findDirectory(FileType.SCENARIO,
            BasePathHolder.getInstance().getBasePath());

        filePath = loadTestDirectory.resolve(fileName);
        fileExists = Files.exists(filePath) && Files.isRegularFile(filePath);

        if (fileExists) {
            Files.delete(filePath);
            JsonUtils.writeJsonResponse(response, Map.of("success", true), 200);

            return;
        }

        loadTestDirectory = FileUtil.findDirectory(FileType.RESULT,
            BasePathHolder.getInstance().getBasePath());

        filePath = loadTestDirectory.resolve(fileName);
        fileExists = Files.exists(filePath) && Files.isRegularFile(filePath);

        if (fileExists) {
            Files.delete(filePath);
            JsonUtils.writeJsonResponse(response, Map.of("success", true), 200);

            return;
        }

        loadTestDirectory = FileUtil.findDirectory(FileType.LOADTEST,
            BasePathHolder.getInstance().getBasePath());

        filePath = loadTestDirectory.resolve(fileName);
        fileExists = Files.exists(filePath) && Files.isRegularFile(filePath);

        if (fileExists) {
            Files.delete(filePath);
            JsonUtils.writeJsonResponse(response, Map.of("success", true), 200);

            return;
        }

        JsonUtils.writeJsonResponse(response, Map.of("success", false),
            HttpServletResponse.SC_SERVICE_UNAVAILABLE);
    }
}
