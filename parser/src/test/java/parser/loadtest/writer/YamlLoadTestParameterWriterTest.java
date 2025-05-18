package parser.loadtest.writer;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apighost.model.loadtest.parameter.LoadTestParameter;
import com.apighost.model.loadtest.parameter.LoadTestParameter.Builder;
import com.apighost.model.loadtest.parameter.Stage;
import com.apighost.parser.loadtest.reader.LoadTestParameterReader;
import com.apighost.parser.loadtest.reader.YamlLoadTestParameterReader;
import com.apighost.parser.loadtest.writer.LoadTestParameterWriter;
import com.apighost.parser.loadtest.writer.YamlLoadTestParameterWriter;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Loadtest Parameter file is well written in yaml
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@DisplayName("YAML load test parameters test")
public class YamlLoadTestParameterWriterTest {

    private static final String TEST_FILE_PATH = "test-loadtest-parameter.yaml";
    private static LoadTestParameterWriter writer = new YamlLoadTestParameterWriter();
    private static LoadTestParameterReader reader = new YamlLoadTestParameterReader();

    @AfterEach
    void tearDown() {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 1. The YAML file is created successfully. 2. The content of the file matches the expected
     * values from the original LoadTestParameter object.
     *
     * @throws IOException if there is an error during writing or reading the YAML file
     */
    @Test
    @DisplayName("LoadTestParameter DTO -> YAML File")
    void YamlLoardTestParamWriterTest() throws IOException {
        Stage stage1 = new Stage.Builder()
            .vus(10)
            .durationMs(30)
            .build();

        Stage stage2 = new Stage.Builder()
            .vus(20)
            .durationMs(60)
            .build();

        LoadTestParameter loadTestParameter = new Builder()
            .name("Test Name")
            .description("This is a test description")
            .thinkTimeMs(1000L)
            .stages(Arrays.asList(stage1, stage2))
            .scenarios(
                Arrays.asList("scenario1.yaml",
                    "scenario2.yaml"))
            .build();

        writer.writeLoadParam(loadTestParameter, TEST_FILE_PATH);

        File outputFile = new File(TEST_FILE_PATH);
        assertTrue(outputFile.exists(), "Output file should be created");

        LoadTestParameter content = reader.readLoadParam(TEST_FILE_PATH);
        assertTrue(content.getName().equals("Test Name"), "The name value does not match");
        assertTrue(content.getDescription().equals("This is a test description"),
            "Description value does not match");
        assertTrue(content.getScenarios().size() > 0, "The scenario is empty");
    }
}
