package controller;

import com.apighost.web.servlet.ApiFrontControllerServlet;
import com.apighost.web.util.FileType;
import com.apighost.web.util.FileUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScenarioExportControllerTest {

    // 테스트 대상 서블릿
    private ApiFrontControllerServlet servlet;

    // 요청/응답 Mock 객체
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    // 응답 출력 스트림
    private StringWriter responseWriter;
    private PrintWriter printWriter;

    // 테스트용 임시 디렉토리
    @TempDir
    File tempDir;

    // 테스트 시나리오 JSON
    private String testScenarioJson;

    @BeforeEach
    void setUp() throws ServletException, IOException {
        // 서블릿 초기화
        servlet = new ApiFrontControllerServlet();
        servlet.init();

        // 테스트 JSON 초기화
        testScenarioJson = "{\n"
                               + "    \"name\": \"Mock User Flow\",\n"
                               + "    \"description\": \"User Registration → Query → Update → Deletion Scenario\",\n"
                               + "    \"timeoutMs\": 10000,\n"
                               + "    \"store\": null,\n"
                               + "    \"steps\": {\n"
                               + "        \"joinUser\": {\n"
                               + "            \"type\": \"HTTP\",\n"
                               + "            \"position\": {\n"
                               + "                \"x\": 100.0,\n"
                               + "                \"y\": 100.0\n"
                               + "            },\n"
                               + "            \"request\": {\n"
                               + "                \"method\": \"POST\",\n"
                               + "                \"url\": \"http://localhost:8080/api/mock/join-user\",\n"
                               + "                \"header\": {\n"
                               + "                    \"Content-Type\": \"application/json\"\n"
                               + "                },\n"
                               + "                \"body\": {\n"
                               + "                    \"formdata\": null,\n"
                               + "                    \"json\": \"{\\n  \\\"name\\\": \\\"youngseok\\\",\\n  \\\"email\\\": \\\"hong@example.com\\\",\\n  \\\"password\\\": \\\"1234\\\"\\n}\\n\"\n"
                               + "                }\n"
                               + "            },\n"
                               + "            \"route\": [\n"
                               + "                {\n"
                               + "                    \"expected\": {\n"
                               + "                        \"status\": \"201\",\n"
                               + "                        \"value\": null\n"
                               + "                    },\n"
                               + "                    \"then\": {\n"
                               + "                        \"store\": {\n"
                               + "                            \"name\": \"youngseok\"\n"
                               + "                        },\n"
                               + "                        \"step\": \"searchUser\"\n"
                               + "                    }\n"
                               + "                }\n"
                               + "            ]\n"
                               + "        },\n"
                               + "        \"searchUser\": {\n"
                               + "            \"type\": \"HTTP\",\n"
                               + "            \"position\": {\n"
                               + "                \"x\": 300.0,\n"
                               + "                \"y\": 100.0\n"
                               + "            },\n"
                               + "            \"request\": {\n"
                               + "                \"method\": \"GET\",\n"
                               + "                \"url\": \"http://localhost:8080/api/mock/search-user?name=youngseok\",\n"
                               + "                \"header\": {\n"
                               + "                    \"Content-Type\": \"application/json\"\n"
                               + "                },\n"
                               + "                \"body\": null\n"
                               + "            },\n"
                               + "            \"route\": [\n"
                               + "                {\n"
                               + "                    \"expected\": {\n"
                               + "                        \"status\": \"200\",\n"
                               + "                        \"value\": {\n"
                               + "                            \"name\": \"Lucy\",\n"
                               + "                            \"age\": \"11\"\n"
                               + "                        }\n"
                               + "                    },\n"
                               + "                    \"then\": {\n"
                               + "                        \"store\": {\n"
                               + "                            \"userId\": 1\n"
                               + "                        },\n"
                               + "                        \"step\": \"modifyUser\"\n"
                               + "                    }\n"
                               + "                }\n"
                               + "            ]\n"
                               + "        }\n"
                               + "    }\n"
                               + "}";

        // 응답 출력 스트림 설정
        responseWriter = new StringWriter();
        printWriter = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    @DisplayName("POST 요청 시 시나리오가 YAML 파일로 정상 저장되는지 확인")
    void testEndToEndScenarioExport() throws ServletException, IOException {
        // === Given (테스트 준비) ===

        // 1. HTTP 요청 모킹
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/apighost/scenario-export");
        when(request.getContextPath()).thenReturn("");
        when(request.getContentType()).thenReturn("application/json");

        // 요청 본문 설정
        BufferedReader reader = new BufferedReader(new StringReader(testScenarioJson));
        when(request.getReader()).thenReturn(reader);

        // 2. FileUtil 모킹
        try (MockedStatic<FileUtil> fileUtilMock = mockStatic(FileUtil.class)) {
            fileUtilMock.when(() -> FileUtil.findDirectory(any(FileType.class)))
                .thenReturn(tempDir);

            // === When (테스트 실행) ===
            servlet.service(request, response);

            // === Then (결과 검증) ===

            // 1. HTTP 응답 설정 확인
            verify(response).setContentType("application/json");
            verify(response).setStatus(HttpServletResponse.SC_OK);

            // 2. 응답 내용 확인
            String responseContent = responseWriter.toString();
            assertTrue(
                responseContent.contains("success") || responseContent.contains("successfully"),
                "응답에 성공 메시지가 포함되어야 합니다");

            // 3. 파일 생성 확인
            File[] files = tempDir.listFiles(
                (dir, name) -> name.startsWith("scenario_Mock User Flow"));
            assertNotNull(files, "시나리오 파일이 생성되어야 합니다");
            assertTrue(files.length > 0, "하나 이상의 파일이 생성되어야 합니다");

            // 4. 파일 내용 확인
            if (files.length > 0) {
                String fileContent = Files.readString(files[0].toPath());
                System.out.println("fileContent : " + fileContent);
                assertTrue(fileContent.contains("name: Mock User Flow"),
                    "YAML 파일에 시나리오 이름이 포함되어야 합니다");
            }
        }
    }
}
