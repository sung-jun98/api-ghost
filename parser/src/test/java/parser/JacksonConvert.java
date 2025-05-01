package parser;

import com.apighost.model.scenario.testfile.Scenario;
import com.apighost.parser.reader.YamlScenarioReader;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import com.apighost.model.scenario.testfile.Position;
import com.apighost.model.scenario.testfile.Request;
import com.apighost.model.scenario.testfile.Response;
import com.apighost.model.scenario.testfile.Step;
import com.apighost.model.scenario.testfile.Then;
import com.apighost.model.scenario.testfile.When;
import java.util.Map;

public class JacksonConvert {

    @Test
    public void yamlFileToDto() throws IOException  {
        String filePath = "src/test/resources/parser/scenarioExample.yaml";

        YamlScenarioReader reader = new YamlScenarioReader();
        Scenario scenario = reader.readScenario(filePath);

        System.out.println("===== 변환된 Scenario 객체 정보 =====");

        // 기본 정보 출력 (null 체크)
        if (scenario.getName() != null) {
            System.out.println("시나리오 이름: " + scenario.getName());
        }

        if (scenario.getDescription() != null) {
            System.out.println("설명: " + scenario.getDescription());
        }

        if (scenario.getScenarioId() != null) {
            System.out.println("ID: " + scenario.getScenarioId());
        }

        if (scenario.getTimeoutMs() != null) {
            System.out.println("타임아웃: " + scenario.getTimeoutMs());
        }

        // variables 출력 (null 체크)
        if (scenario.getVariables() != null) {
            System.out.println("\n===== 변수 목록 =====");
            for (Map.Entry<String, Object> entry : scenario.getVariables().entrySet()) {
                if (entry.getValue() != null) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            }
        }

        // steps 출력 (null 체크)
        if (scenario.getSteps() != null) {
            System.out.println("\n===== 스텝 목록 =====");
            for (Map.Entry<String, Step> entry : scenario.getSteps().entrySet()) {
                String stepName = entry.getKey();
                Step step = entry.getValue();

                if (step != null) {
                    System.out.println("\n## 스텝: " + stepName);

                    if (step.getType() != null) {
                        System.out.println("타입: " + step.getType());
                    }

                    // position 출력
                    if (step.getPosition() != null) {
                        Position pos = step.getPosition();
                        if (pos.getX() != null && pos.getY() != null) {
                            System.out.println("위치: x=" + pos.getX() + ", y=" + pos.getY());
                        }
                    }

                    // request 출력
                    if (step.getRequest() != null) {
                        Request req = step.getRequest();
                        System.out.println("요청 정보:");

                        if (req.getMethod() != null) {
                            System.out.println("  메서드: " + req.getMethod());
                        }

                        if (req.getUrl() != null) {
                            System.out.println("  URL: " + req.getUrl());
                        }

                        // headers 출력
                        if (req.getHeaders() != null && !req.getHeaders().isEmpty()) {
                            System.out.println("  헤더:");
                            for (Map.Entry<String, String> header : req.getHeaders().entrySet()) {
                                if (header.getValue() != null) {
                                    System.out.println("    " + header.getKey() + ": " + header.getValue());
                                }
                            }
                        }

                        // body 출력
                        if (req.getBody() != null && !req.getBody().isEmpty()) {
                            System.out.println("  바디:");
                            for (Map.Entry<String, Map<String, Object>> bodyField : req.getBody().entrySet()) {
                                if (bodyField.getValue() != null) {
                                    System.out.println("    " + bodyField.getKey() + ":");
                                    for (Map.Entry<String, Object> typeValue : bodyField.getValue().entrySet()) {
                                        if (typeValue.getValue() != null) {
                                            System.out.println("      " + typeValue.getKey() + ": " + typeValue.getValue());
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // response 출력
                    if (step.getResponse() != null && !step.getResponse().isEmpty()) {
                        System.out.println("응답 정보:");
                        for (int i = 0; i < step.getResponse().size(); i++) {
                            Response response = step.getResponse().get(i);
                            if (response != null) {
                                System.out.println("  응답 #" + (i+1));

                                // when 출력
                                if (response.getWhen() != null) {
                                    When when = response.getWhen();
                                    System.out.println("    When 조건:");

                                    if (when.getStatus() != null) {
                                        System.out.println("      상태: " + when.getStatus());
                                    }

                                    if (when.getCondition() != null) {
                                        System.out.println("      조건: " + when.getCondition());
                                    }

                                    if (when.getBody() != null && !when.getBody().isEmpty()) {
                                        System.out.println("      바디:");
                                        for (Map.Entry<String, Object> bodyField : when.getBody().entrySet()) {
                                            if (bodyField.getValue() != null) {
                                                System.out.println("        " + bodyField.getKey() + ": " + bodyField.getValue());
                                            }
                                        }
                                    }
                                }

                                // then 출력
                                if (response.getThen() != null) {
                                    Then then = response.getThen();
                                    System.out.println("    Then 액션:");

                                    if (then.getNext() != null) {
                                        System.out.println("      다음 스텝: " + then.getNext());
                                    }

                                    if (then.getSave() != null && !then.getSave().isEmpty()) {
                                        System.out.println("      저장 변수:");
                                        for (Map.Entry<String, String> saveField : then.getSave().entrySet()) {
                                            if (saveField.getValue() != null) {
                                                System.out.println("        " + saveField.getKey() + ": " + saveField.getValue());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
