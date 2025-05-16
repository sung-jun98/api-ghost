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

public class ErrorHandlingFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

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

    private void handleException(HttpServletResponse response, Throwable e) throws IOException {
        ErrorCode errorCode = resolveErrorCode(e);
        String message = e.getMessage();
        if (message != null && !message.trim().isEmpty() && !message.toLowerCase().contains("null")) {
            JsonUtils.writeErrorResponse(response, errorCode, message);
        } else {
            JsonUtils.writeErrorResponse(response, errorCode);
        }
    }

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

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
