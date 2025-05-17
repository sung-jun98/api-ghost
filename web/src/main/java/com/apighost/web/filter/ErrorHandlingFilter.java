package com.apighost.web.filter;

import com.apighost.web.util.ErrorCode;
import com.apighost.web.util.JsonUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 * A servlet filter that handles uncaught exceptions during request processing and
 * writes appropriate JSON error responses based on the exception type.
 *
 * <p>This filter intercepts any thrown exceptions from downstream filters or servlets,
 * resolves them to a predefined {@link ErrorCode}, and writes a structured JSON response
 * using {@link JsonUtils}.
 *
 * <p>Typical usage involves registering this filter in a Spring Boot or servlet container
 * context to ensure consistent error responses across the application.
 *
 *
 * @author oneweeek
 * @version BETA-0.0.1
 */
public class ErrorHandlingFilter implements Filter {

    /**
     * Initializes the filter. No specific configuration is applied in this implementation.
     *
     * @param filterConfig the filter configuration provided by the container
     * @throws ServletException if an initialization error occurs
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    /**
     * Applies filtering logic by forwarding the request and catching any thrown exceptions.
     * On exception, it handles the error by resolving an appropriate {@link ErrorCode} and
     * writing a JSON response.
     *
     * @param request the incoming {@link ServletRequest}
     * @param response the outgoing {@link ServletResponse}
     * @param filterChain the filter chain to pass the request along
     * @throws IOException if an I/O error occurs during processing
     * @throws ServletException if a servlet-specific error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handleException(httpResponse, e);
        }
    }

    /**
     * Resolves the error code from the given exception and writes the error response
     * using {@link JsonUtils}. Includes the exception message if it is non-empty and meaningful.
     *
     * @param response the {@link HttpServletResponse} to write to
     * @param e the exception to handle
     * @throws IOException if an error occurs while writing the response
     */
    private void handleException(HttpServletResponse response, Throwable e) throws IOException {
        ErrorCode errorCode = resolveErrorCode(e);
        String message = e.getMessage();
        if (message != null && !message.trim().isEmpty() && !message.toLowerCase().contains("null")) {
            JsonUtils.writeErrorResponse(response, errorCode, message);
        } else {
            JsonUtils.writeErrorResponse(response, errorCode);
        }
    }

    /**
     * Maps the given exception to a corresponding {@link ErrorCode}.
     *
     * @param e the exception to resolve
     * @return the resolved {@link ErrorCode}
     */
    private ErrorCode resolveErrorCode(Throwable e) {
        if (e instanceof FileNotFoundException) {
            return ErrorCode.FILE_NOT_FOUND;
        }
        if (e instanceof UnsupportedOperationException){
            return ErrorCode.METHOD_NOT_ALLOWED;
        }
        if (e instanceof IllegalArgumentException) {
            if (e.getMessage() != null && e.getMessage().contains("Controller not found")) {
                return ErrorCode.CONTROLLER_NOT_FOUND;
            }
            return ErrorCode.INVALID_PARAMETER;
        }
        if (e instanceof IllegalStateException) {
            return ErrorCode.ILLEGAL_STATE;
        }
        if (e instanceof IOException) {
            return ErrorCode.IO_ERROR;
        }
        if (e instanceof Exception) {
            return ErrorCode.INTERNAL_SERVER_ERROR;
        }
        return ErrorCode.INTERNAL_SERVER_ERROR;
    }

    /**
     * Destroys the filter. No cleanup is required in this implementation.
     */
    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
