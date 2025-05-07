package com.apighost.cli.command;

import com.apighost.cli.util.ConsoleOutput;
import com.apighost.cli.util.FileUtil;
import com.apighost.model.scenario.ScenarioResult;
import com.apighost.parser.scenario.reader.JsonScenarioResultReader;
import java.io.File;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * @author sun-jun98
 * @version BETA-0.0.1
 */
@Command(
    name = "cat",
    description = "Open a JSON Result file on console",
    mixinStandardHelpOptions = true
)
public class ViewResultCommand implements Callable<Integer> {

    @Parameters(
        index = "0",
        description = "The name of the JSON file to read",
        arity = "1"
    )
    private String fileName;

    @Override
    public Integer call() throws Exception {

        if (fileName == null) {
            ConsoleOutput.printError("Please enter file name >> ");
            return 1;
        }

        File targetDir = FileUtil.findDirectory(FileType.RESULTS.toString());
        File targetFile = new File(targetDir, fileName);

        JsonScenarioResultReader reader = new JsonScenarioResultReader();
        ScenarioResult result = reader.readScenarioResult(targetFile.toString());

        ConsoleOutput.printBold("<===================TEST RESULT=====================>");
        ConsoleOutput.print(result.getName());
        ConsoleOutput.print(result.getDescription());
        ConsoleOutput.print(result.getExecutedAt());
        if (result.getIsScenarioSuccess()) {
            ConsoleOutput.printBold("TEST PASS");
        }
        ConsoleOutput.print("Total Duration Time : " + String.valueOf(result.getTotalDurationMs()) + "ms");
        ConsoleOutput.print("Average Duration Time : " + String.valueOf(result.getAverageDurationMs()) + "ms");

        return 0;
    }
}
