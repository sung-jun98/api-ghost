package com.apighost.cli.command;

import com.apighost.cli.publisher.BufferedCliPublisher;
import com.apighost.cli.util.ConsoleOutput;
import com.apighost.loadtest.publisher.ResultPublisher;
import com.apighost.model.loadtest.parameter.LoadTestExecuteParameter;
import com.apighost.model.loadtest.parameter.LoadTestParameter;
import com.apighost.parser.loadtest.converter.LoadTestParameterConverter;
import com.apighost.parser.loadtest.reader.LoadTestParameterReader;
import com.apighost.parser.loadtest.reader.YamlLoadTestParameterReader;
import com.apighost.parser.scenario.reader.ScenarioReader;
import com.apighost.parser.scenario.reader.YamlScenarioReader;
import com.apighost.util.file.BasePathHolder;
import com.apighost.util.file.FileType;
import com.apighost.util.file.FileUtil;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * Command class for executing an API Ghost load test via the CLI. * * <p>Usage example:</p>
 * * <pre>{@code
 *  * apighost loadtest load-test.yaml
 *  * }</pre>
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@Command(
    name = "loadtest",
    description = "Start the API Ghost load test",
    mixinStandardHelpOptions = true
)
public class ExecuteLoadTestCommand implements Callable<Integer> {

    @Parameters(
        index = "0",
        description = "File name to start the scenario test",
        arity = "1"
    )
    private String fileName;

    private final ResultPublisher publisher = new BufferedCliPublisher();
    private static LoadTestParameterReader reader;
    private static LoadTestParameter loadTestParameter;
    private static LoadTestParameterConverter converter;
    private static ScenarioReader scenarioReader;

    /**
     * 1. `.apighost/LOADTEST` Check if there is a filename inside the folder <br>
     * 2. Change the file to DTO. <br>
     * 3. Change the changed DTO to LoadtestExecuteparam. <br>
     * 4. Run Orchestrator based on the changed param.
     *
     * @return End code. Success if 0 is success, 1 fails
     * @throws Exception
     */
    @Override
    public Integer call() throws Exception {

        ConsoleOutput.print("Starting load test with file: " + fileName);
        ConsoleOutput.print("Press Ctrl+C to stop the test...");

        if (fileName != null && (fileName.endsWith(".yaml") || fileName.endsWith(
            ".yml"))) {
            reader = new YamlLoadTestParameterReader();
            scenarioReader = new YamlScenarioReader();
            converter = new LoadTestParameterConverter(scenarioReader);
        } else {
            throw new IllegalArgumentException("It is the form of the wrong file.");
        }

        Path loadTestDirectory = FileUtil.findDirectory(FileType.LOADTEST,
            BasePathHolder.getInstance().getBasePath());

        Path filePath = loadTestDirectory.resolve(fileName);

        boolean fileExists = Files.exists(filePath) && Files.isRegularFile(filePath);

        if (!fileExists) {
            throw new FileNotFoundException("There is no file.: " + fileName);

        } else {
            loadTestParameter = reader.readLoadParam(
                filePath.toAbsolutePath().toString());
        }

        LoadTestExecuteParameter executeParameter = converter.convert(loadTestParameter);

//        LoadTestOrchestrator orchestrator = new LoadTestOrchestrator(publisher);
//        orchestrator.start(executeParameter);

        return 0;
    }
}
