package com.apighost.cli.command;

import com.apighost.cli.util.ConsoleOutput;
import com.apighost.cli.util.FileUtil;
import java.io.File;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;

/**
 * A command that reads the current YAML scenario files. The result is displayed in the user's
 * console window.
 * <p>
 * Example Usage : `apighost view-scenario`
 *
 * @author sun-jun98
 * @version BETA-0.0.1
 */
@Command(
    name = "ls",
    description = "View YAML file content",
    mixinStandardHelpOptions = true,
    subcommands = {ViewResultListCommand.class}
)
public class ViewScenarioListCommand implements Callable<Integer> {

    /**
     * In the user's specific local directory, only YAML files are found and the list is shown in
     * the user's console window.
     *
     * @return Integer If 0 is success 1 fails if it is success 1
     * @throws Exception
     */
    @Override
    public Integer call() throws Exception {
        try {
            File targetDir = FileUtil.findDirectory(FileType.SCENARIO);

            /** List files with .yaml or .yml extensions only */
            File[] files = targetDir.listFiles((dir, name) ->
                                                   name.toLowerCase().endsWith(".yaml") ||
                                                       name.toLowerCase().endsWith(".yml")
            );

            /**
             * Check if any YAML files were found
             * Return 0 even if no files found
             */
            if (files == null || files.length == 0) {
                ConsoleOutput.printError("No Scenario Test files found.");
                ConsoleOutput.print("Use `apighost edit --create` command to create a new file");

                return 0;
            }

            /** Scenario file print */
            ConsoleOutput.printBold(
                "\n<========= Scenario Files List (" + files.length + " files) =========>");

            for (File file : files) {
                ConsoleOutput.printBold(file.getName());
            }

            return 0;

        } catch (Exception e) {

            ConsoleOutput.printErrorBold("Error occurred while searching files: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
