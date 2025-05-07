package com.apighost.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ApiController {

    default void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    default void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    default void doPut(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    default void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}

