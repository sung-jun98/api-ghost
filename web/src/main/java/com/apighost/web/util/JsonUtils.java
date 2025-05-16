package com.apighost.web.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * Utility class for writing JSON responses to {@link HttpServletResponse} objects.
 *
 * <p>This class provides methods to serialize objects into JSON using Jackson
 * and write them directly to the HTTP response stream. It also includes
 * specialized methods for writing standardized error responses using {@link ErrorCode}.
 *
 * <p>All responses are written with UTF-8 encoding and the
 * <code>application/json</code> content type.
 *
 * @author sun-jun98
 * @version BETA-0.0.1
 */
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper()
                                                         .enable(
                                                             SerializationFeature.INDENT_OUTPUT);
    /**
     * Writes a JSON response to the given {@link HttpServletResponse} using the specified object and status code.
     *
     * @param response the HTTP response to write to
     * @param object the object to serialize as JSON
     * @param statusCode the HTTP status code to set
     * @throws IOException if an I/O error occurs while writing the response
     */
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

    /**
     * Writes a JSON-formatted error response using the specified {@link ErrorCode}.
     * The default message from the error code will be used.
     *
     * @param response the HTTP response to write to
     * @param errorCode the error code indicating the type of error
     * @throws IOException if an I/O error occurs while writing the response
     */
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

    /**
     * Writes a JSON-formatted error response using the specified {@link ErrorCode}
     * and a custom error message.
     *
     * @param response the HTTP response to write to
     * @param errorCode the error code indicating the type of error
     * @param message the custom error message to include in the response
     * @throws IOException if an I/O error occurs while writing the response
     */
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

    /**
     * Represents a standardized error response object used for JSON serialization.
     */
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
