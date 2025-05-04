package parser.scenario.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apighost.model.scenario.Scenario;
import com.apighost.parser.scenario.reader.YamlScenarioReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.apighost.model.scenario.step.Step;
import com.apighost.model.scenario.request.FormData;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit tests for {@link YamlScenarioReader}.
 * <p>
 * This test verifies that the reader can correctly deserialize a Yaml file into a {@link Scenario}
 * object, and that it handles invalid paths gracefully.
 * </p>
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class YamlScenarioReaderTest {

    @TempDir
    Path tempDir;

    static ObjectMapper objectMapper = new ObjectMapper();

    static String YAML_INPUT = """
        name: string
        description: string
        timeoutMs: 1000
        
        store:
          valid1: hello
        
        steps:
          stepNameB:
            type: HTTP
            position:
              x: 100.0
              y: 200.1
        
            request:
              method: POST
              url: string
              header:
                headerName: value
                destination: string
                subscription: string
                message: string
                action: string
              body:
                json: |
                  {
                    "userId": "user_12345",
                      "preferences": {
                        "language": "ko",
                        "notifications": [{
                          "email": true,
                          "sms": false,
                          "push": {
                            "enabled": true,
                            "frequency": "daily"
                          }
                        }]
                      }
                    }
                  }
        
            route:
              - expected:
                  status: "200"
                  value:
                    fieldName: value
        
                then:
                  store:
                    variableName: value
                  step: string
        
        
          stepNameA:
            type: HTTP
            position:
              x: 100.0
              y: 200.1
        
            request:
              method: POST
              url: string
              header:
                headerName: value
                destination: string
                subscription: string
                message: string
                action: string
              body:
                formdata:
                  file:
                    userImage: "/Users/hajiwon/Downloads/image.png"
                    userTextFile: "/Users/hajiwon/Downloads/image.txt"
                  text:
                    userText: |
                      {
                        "productCategoryId": 2,
                        "storeId": 1,
                        "name": "AAA2",
                        "price": 3000,
                        "status": true,
                        "productType": "EXTRA",
                        "productSize": "F"
                      }
                    userJson: |
                      {
                        "productCategoryId": 2,
                        "storeId": 1,
                        "name": "AAAA",
                        "price": 3000,
                        "status": true,
                        "productType": "EXTRA",
                        "productSize": "F"
                      }
        
            route:
              - expected:
                  status: "200"
                  value:
                    fieldName: value
        
                then:
                  store:
                    variableName: value
                  step: string
        """;


    /**
     * Method testing read as a DTO based on the YAML file
     *
     * @throws IOException if reading the test file fails
     */
    @Test
    @DisplayName("Should convert basic YAML file to Scenario DTO")
    void readScenarioTest() throws IOException {
        /** given */
        Path tempFile = tempDir.resolve("test-scenario.yaml");
        Files.writeString(tempFile, YAML_INPUT);

        YamlScenarioReader reader = new YamlScenarioReader();

        /** when */
        Scenario scenario = reader.readScenario(tempFile.toString());
        String jsonOutput = objectMapper.writerWithDefaultPrettyPrinter()
            .writeValueAsString(scenario);

        /** then */
        assertNotNull(scenario, "Scenario object must not be null");
        assertNotNull(scenario.getName(), "Scenario name must not be null");
        assertNotNull(scenario.getSteps(), "Steps must not be null");
        assertFalse(scenario.getSteps().isEmpty(), "Steps must not be empty");

        if (scenario.getStore() != null) {
            assertFalse(scenario.getStore().isEmpty(), "Store must not be empty if present");
        }

        if (scenario.getTimeoutMs() != null) {
            assertTrue(scenario.getTimeoutMs() > 0, "Timeout must be positive if present");
        }
    }

    /**
     * Should correctly parse FormData elements in YAML Print the whole content with the console.
     *
     * @throws IOException if reading the test file fails
     */
    @Test
    @DisplayName("Should correctly parse FormData elements in YAML")
    void readScenarioFormDataTest() throws IOException {
        /** given */
        Path tempFile = tempDir.resolve("test-scenario.yaml");
        Files.writeString(tempFile, YAML_INPUT);

        YamlScenarioReader reader = new YamlScenarioReader();
        ObjectMapper objectMapper = new ObjectMapper();

        /** when */
        Scenario scenario = reader.readScenario(tempFile.toString());
        String jsonOutput = objectMapper.writerWithDefaultPrettyPrinter()
            .writeValueAsString(scenario);

        System.out.println(scenario.toString());

        /** then */
        Step step = scenario.getSteps().get("stepNameA");
        FormData formData = step.getRequest().getBody().getFormdata();

        assertNotNull(formData.getFile(), "File property must exist");
        assertEquals("/Users/hajiwon/Downloads/image.png", formData.getFile().get("userImage"));
        assertEquals("/Users/hajiwon/Downloads/image.txt", formData.getFile().get("userTextFile"));

        assertNotNull(formData.getText(), "Text property must exist");
        assertTrue(formData.getText().containsKey("userText"));
        assertTrue(formData.getText().containsKey("userJson"));
    }
}
