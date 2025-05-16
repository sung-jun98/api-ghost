package com.apighost.web.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper()
                                                         .enable(
                                                             SerializationFeature.INDENT_OUTPUT);

    public static void writeJsonResponse(HttpServletResponse response, Object object, int statusCode)
        throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);

        String jsonString = objectMapper.writeValueAsString(object);
        PrintWriter writer = response.getWriter();
        writer.write(jsonString);
        writer.flush();
    }

    public static void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode)
        throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getHttpStatus());

        String jsonString = objectMapper.writeValueAsString(
            new ErrorResponse(errorCode));
        PrintWriter writer = response.getWriter();
        writer.write(jsonString);
        writer.flush();
    }

    public static void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode, String message)
        throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getHttpStatus());

        String jsonString = objectMapper.writeValueAsString(
            new ErrorResponse(errorCode, message));
        PrintWriter writer = response.getWriter();
        writer.write(jsonString);
        writer.flush();
    }

    private static class ErrorResponse {
        private final int status;
        private final String message;

        public ErrorResponse(ErrorCode errorCode, String message) {
            this.status = errorCode.getHttpStatus();
            this.message = message;
        }

        public ErrorResponse(ErrorCode errorCode) {
            this.status = errorCode.getHttpStatus();
            this.message = errorCode.getMessage();
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
