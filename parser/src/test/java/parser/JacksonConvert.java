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
    public void yamlFileToDto() throws IOException {
        String filePath = "src/test/resources/parser/scenarioExample.yaml";

        YamlScenarioReader reader = new YamlScenarioReader();
        Scenario scenario = reader.readScenario(filePath);

        System.out.println("===== converted Scenario information =====");

        if (scenario.getName() != null) {
            System.out.println("Scenario name: " + scenario.getName());
        }

        if (scenario.getDescription() != null) {
            System.out.println("Description: " + scenario.getDescription());
        }

        if (scenario.getScenarioId() != null) {
            System.out.println("ScenarioId: " + scenario.getScenarioId());
        }

        if (scenario.getTimeoutMs() != null) {
            System.out.println("TimeoutMs: " + scenario.getTimeoutMs());
        }

        if (scenario.getVariables() != null) {
            System.out.println("\n===== Varialbe List =====");
            for (Map.Entry<String, Object> entry : scenario.getVariables().entrySet()) {
                if (entry.getValue() != null) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            }
        }

        if (scenario.getSteps() != null) {
            System.out.println("\n===== steps list =====");
            for (Map.Entry<String, Step> entry : scenario.getSteps().entrySet()) {
                String stepName = entry.getKey();
                Step step = entry.getValue();

                if (step != null) {
                    System.out.println("\n## steps: " + stepName);

                    if (step.getType() != null) {
                        System.out.println("type: " + step.getType());
                    }

                    if (step.getPosition() != null) {
                        Position pos = step.getPosition();
                        if (pos.getX() != null && pos.getY() != null) {
                            System.out.println("position: x=" + pos.getX() + ", y=" + pos.getY());
                        }
                    }

                    if (step.getRequest() != null) {
                        Request req = step.getRequest();
                        System.out.println("request Information:");

                        if (req.getMethod() != null) {
                            System.out.println("  method: " + req.getMethod());
                        }

                        if (req.getUrl() != null) {
                            System.out.println("  URL: " + req.getUrl());
                        }

                        if (req.getHeaders() != null && !req.getHeaders().isEmpty()) {
                            System.out.println("  header:");
                            for (Map.Entry<String, String> header : req.getHeaders().entrySet()) {
                                if (header.getValue() != null) {
                                    System.out.println(
                                        "    " + header.getKey() + ": " + header.getValue());
                                }
                            }
                        }

                        if (req.getBody() != null && !req.getBody().isEmpty()) {
                            System.out.println("  body:");
                            for (Map.Entry<String, Map<String, Object>> bodyField : req.getBody()
                                                                                        .entrySet()) {
                                if (bodyField.getValue() != null) {
                                    System.out.println("    " + bodyField.getKey() + ":");
                                    for (Map.Entry<String, Object> typeValue : bodyField.getValue()
                                                                                   .entrySet()) {
                                        if (typeValue.getValue() != null) {
                                            System.out.println("      " + typeValue.getKey() + ": "
                                                                   + typeValue.getValue());
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (step.getResponse() != null && !step.getResponse().isEmpty()) {
                        System.out.println("response information:");
                        for (int i = 0; i < step.getResponse().size(); i++) {
                            Response response = step.getResponse().get(i);
                            if (response != null) {
                                System.out.println("  response #" + (i + 1));

                                // when 출력
                                if (response.getWhen() != null) {
                                    When when = response.getWhen();
                                    System.out.println("    When condition:");

                                    if (when.getStatus() != null) {
                                        System.out.println("      status: " + when.getStatus());
                                    }

                                    if (when.getCondition() != null) {
                                        System.out.println(
                                            "      condition: " + when.getCondition());
                                    }

                                    if (when.getBody() != null && !when.getBody().isEmpty()) {
                                        System.out.println("      body:");
                                        for (Map.Entry<String, Object> bodyField : when.getBody()
                                                                                       .entrySet()) {
                                            if (bodyField.getValue() != null) {
                                                System.out.println(
                                                    "        " + bodyField.getKey() + ": "
                                                        + bodyField.getValue());
                                            }
                                        }
                                    }
                                }

                                if (response.getThen() != null) {
                                    Then then = response.getThen();
                                    System.out.println("    Then :");

                                    if (then.getNext() != null) {
                                        System.out.println("      next step: " + then.getNext());
                                    }

                                    if (then.getSave() != null && !then.getSave().isEmpty()) {
                                        System.out.println("      save Variable:");
                                        for (Map.Entry<String, String> saveField : then.getSave()
                                                                                       .entrySet()) {
                                            if (saveField.getValue() != null) {
                                                System.out.println(
                                                    "        " + saveField.getKey() + ": "
                                                        + saveField.getValue());
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
