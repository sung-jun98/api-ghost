package com.apighost.web.util;
/**
 * Defines a set of error codes used for standardized error responses across the application.
 *
 * <p>Each error code is associated with an HTTP status code and a default error message.
 * These values are used to generate consistent JSON error responses for various failure scenarios.
 *
 * <p>Typical usage:
 * <pre>
 *     ErrorCode errorCode = ErrorCode.INVALID_PARAMETER;
 *     int status = errorCode.getHttpStatus();
 *     String message = errorCode.getMessage();
 * </pre>
 *
 * @author oneweeek
 * @version BETA-0.0.1
 */
public enum ErrorCode {
    INVALID_PARAMETER(400, "Invalid parameter."),
    INVALID_JSON_FORMAT(400, "Invalid JSON format."),
    RESOURCE_NOT_FOUND(404, "The requested resource was not found."),
    CONTROLLER_NOT_FOUND(404,  "The requested controller was not found."),
    FILE_NOT_FOUND(404, "The requested file was not found."),
    METHOD_NOT_ALLOWED(405,  "Method not allowed."),
    ILLEGAL_STATE(409, "Invalid state change requested."),
    IO_ERROR(500, "An error occurred during an input/output operation."),
    CLASS_NOT_FOUND(500, "The requested class was not found."),
    INTERNAL_SERVER_ERROR(500, "An internal server error has occurred.");

    private final int httpStatus;
    private final String message;

    /**
     * Constructs an ErrorCode with the specified HTTP status, code, and message.
     *
     * @param httpStatus the HTTP status associated with the error
     * @param message the default error message
     */
    ErrorCode(int httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    /**
     * Gets the HTTP status associated with the error.
     *
     * @return the HTTP status
     */
    public int getHttpStatus() {
        return httpStatus;
    }

    /**
     * Gets the default error message.
     *
     * @return the error message
     */
    public String getMessage() {
        return message;
    }
}
