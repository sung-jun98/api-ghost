package com.apighost.web.sevlet;

import com.apighost.web.controller.ApiController;
import com.apighost.web.controller.ScenarioListController;
import com.apighost.web.util.JsonUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ApiFrontControllerServlet extends HttpServlet {

    private final Map<String, ApiController> controllerMap = new HashMap<>();

    @Override
    public void init() throws ServletException {
        // 컨트롤러 등록
        controllerMap.put("scenario-list", new ScenarioListController());
//        controllerMap.put("result-list", new ResultListController());
//        controllerMap.put("scenario-info", new ScenarioInfoController());
//        controllerMap.put("result-info", new ResultInfoController());
//        controllerMap.put("endpoint-json", new EndpointJsonController());
//        controllerMap.put("scenario-test", new ScenarioTestController());
    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        // URL 경로 분석
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestURI.substring(contextPath.length());

        // /apighost/xxx 형태의 URL만 처리
        if (!path.startsWith("/apighost/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 컨트롤러 키 추출 (/apighost/scenario-list -> scenario-list)
        String controllerKey = path.substring("/apighost/".length());

        // 컨트롤러 조회
        ApiController controller = controllerMap.get(controllerKey);
        if (controller == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 요청 처리
        try {
            switch (request.getMethod()) {
                case "GET":
                    controller.doGet(request, response);
                    break;
                case "POST":
                    controller.doPost(request, response);
                    break;
                case "PUT":
                    controller.doPut(request, response);
                    break;
                case "DELETE":
                    controller.doDelete(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        } catch (Exception e) {
            // 예외 처리 - 클라이언트에게 오류 메시지 전송
            JsonUtils.writeErrorResponse(response, "Server error: " + e.getMessage(),
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}