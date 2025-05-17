package loadtest.reader;


import static java.nio.file.Files.createTempFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apighost.model.loadtest.parameter.LoadTestParameter;
import com.apighost.parser.loadtest.reader.YamlLoadTestParameterReader;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Loadtest Parameter YAML file is well converted to Java object
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class YamlLoadTestParameterReaderTest {

    @TempDir
    Path tempDir;

    private YamlLoadTestParameterReader reader;

    @BeforeEach
    void setUp() {
        reader = new YamlLoadTestParameterReader();
    }

    static String YAML_INPUT = """
        name: "TEST"
        description: "TEST DESCRIPTION"
        thinkTimeMs: 1000
        stage:
          - vus: 100
            duration: 60
          - vus: 2000
            duration: 100
          - vus: 500
            duration: 50
        scenarios:
          - scenario1.yaml
          - scenario2.yaml
        """;

    @Test
    @DisplayName("ReaderTest")
    void readYamlLoadTestParam() throws IOException {
        Path scenarioPath = createTempFile("test-loadtest", ".yaml");
        java.nio.file.Files.writeString(scenarioPath, YAML_INPUT);

        /** when */
        LoadTestParameter parameter = reader.readLoadParam(scenarioPath.toString());

        /** then */
        assertNotNull(parameter);
        assertEquals("TEST", parameter.getName());
        assertEquals("TEST DESCRIPTION",
            parameter.getDescription());

        assertTrue(parameter.getStages().size() > 0);
    }
}
