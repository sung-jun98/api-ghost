package com.apighost.cli.command;

import com.apighost.cli.callback.CliScenarioResultCallback;
import com.apighost.cli.util.ConsoleOutput;
import com.apighost.cli.util.FileUtil;
import com.apighost.model.scenario.Scenario;
import com.apighost.model.scenario.ScenarioResult;
import com.apighost.parser.scenario.reader.ScenarioReader;
import com.apighost.parser.scenario.reader.YamlScenarioReader;
import com.apighost.parser.scenario.writer.JsonScenarioResultWriter;
import com.apighost.parser.scenario.writer.ScenarioResultWriter;
import com.apighost.scenario.executor.ScenarioTestExecutor;
import java.io.File;
import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * Command class for executing an API Ghost scenario test via the CLI.
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * apighost execute scenario-test.yaml
 * }</pre>
 *
 * <p>Supported file formats:</p>
 * <ul>
 *   <li>.yaml</li>
 *   <li>.yml</li>
 * </ul>
 *
 * @author haazz
 * @version BETA-0.0.1
 */
@Command(
    name = "execute",
    description = "Start the API Ghost scenario test",
    mixinStandardHelpOptions = true
)
public class ExecuteScenairoCommand implements Callable<Integer> {

    /**
     * The name of the scenario file to be executed.
     * <p>
     * located in the SCENARIO directory.
     * </p>
     */
    @Parameters(
        index = "0",
        description = "File name to start the scenario test",
        arity = "1"
    )
    private String fileName;

    /**
     * Executes the scenario test.
     * <p>
     * This method validates the file name, loads the scenario, prints a logo, executes the
     * scenario, and saves the result.
     * </p>
     *
     * @return exit code {@code 0} if the execution was successful, or {@code 1} on error
     * @throws Exception if any file I/O or parsing issues occur
     */
    @Override
    public Integer call() throws Exception {
        if (fileName == null || fileName.trim().isEmpty()) {
            ConsoleOutput.printError(
                "Please enter file name >> ex) apighost execute scenario-test.yaml");
            return 1;
        }

        Scenario scenario;
        ScenarioReader scenarioReader;
        if (fileName.endsWith(".yaml") || fileName.endsWith(".yml")) {
            scenarioReader = new YamlScenarioReader();
        } else {
            ConsoleOutput.printError(
                "Invailid file format >> ex) apighost execute scenario-test.yaml");
            return 1;
        }
        File scenarioDir = FileUtil.findDirectory(FileType.SCENARIO);
        File scenarioFile = new File(scenarioDir, fileName);
        scenario = scenarioReader.readScenario(scenarioFile.getPath());

        ConsoleOutput.printLogo();
        ScenarioTestExecutor scenarioTestExecutor = new ScenarioTestExecutor();
        ScenarioResult scenarioResult = scenarioTestExecutor.execute(scenario,
            new CliScenarioResultCallback());

        ScenarioResultWriter scenarioResultWriter = new JsonScenarioResultWriter();
        File scenarioResultDir = FileUtil.findDirectory(FileType.RESULT);
        File scenarioResultFile = new File(scenarioResultDir,
            FileUtil.replaceIllegalFileName(scenario.getName() + LocalDateTime.now() + ".json"));
        scenarioResultWriter.writeScenarioResult(scenarioResult, scenarioResultFile.getPath());
        return 0;
    }
}