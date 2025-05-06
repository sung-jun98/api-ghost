package com.apighost.cli.command;

import com.apighost.cli.util.ConsoleOutput;
import com.apighost.cli.util.FileUtil;
import java.io.File;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;

/**
 * A command that reads the scenario resulls files. The result is displayed in the user's console
 * window.
 * <p>
 * Example Usage : `apighost ls results`
 * </p>
 *
 * @author sun-jun98
 * @version BETA-0.0.1
 */
@Command(
    name = "results",
    description = "List result JSON files",
    mixinStandardHelpOptions = true
)
public class ViewResultListCommand implements Callable<Integer> {

    /**
     * In the user's specific local directory, only json files are found and the list is shown in
     * the user's console window.
     *
     * @return Integer If 0 is success 1 fails if it is success 1
     * @throws RuntimeException
     */
    @Override
    public Integer call() throws RuntimeException {
        try {
            File targetDir = FileUtil.findDirectory(FileType.RESULTS.toString());

            /** List files with .json extensions only */
            File[] files = targetDir.listFiles((dir, name) ->
                                                   name.toLowerCase().endsWith(".json")
            );

            /** Check if any json files were found */
            if (files == null || files.length == 0) {
                ConsoleOutput.printError("No Test Reports found.");
                return 0;
            }

            ConsoleOutput.printBold(
                "\n<========= Test Result Files List (" + files.length + " files) =========>");

            for (File file : files) {
                ConsoleOutput.print(file.getName());
            }

            return 0;

        } catch (RuntimeException e) {
            ConsoleOutput.printError("Error occurred while searching files: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
