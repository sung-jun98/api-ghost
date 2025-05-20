package com.apighost.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

public class StaticResourceServlet extends HttpServlet {

    private static final String RESOURCE_PREFIX = "static/";
    private static final String DEFAULT_FILE = "index.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        String path = req.getPathInfo();
        if (path == null || path.equals("/") || path.isEmpty()) {
            path = DEFAULT_FILE;
        } else {
            path = path.startsWith("/") ? path.substring(1) : path;
        }

        String resourcePath = RESOURCE_PREFIX + path;

        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                resp.sendError(404, "Resource not found: " + resourcePath);
                return;
            }

            String mime = URLConnection.guessContentTypeFromName(path);
            if (mime != null) {
                resp.setContentType(mime);
            }

            in.transferTo(resp.getOutputStream());
        }
    }
}
