package com.apighost.cli.command;

import static com.apighost.cli.util.FileUtil.findProjectRoot;

import com.apighost.cli.util.FileUtil;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Stream;
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
        /** Get current working directory */
        String currentDir = System.getProperty("user.dir");

        /** Find project root directory */
        FileUtil fileUtil = new FileUtil();
        Optional<File> projectRootOpt = fileUtil.findProjectRoot(new File(currentDir));

        if (projectRootOpt.isEmpty()) {
            System.out.println("Unable to find project root directory");
            return 1;
        }

        File projectRoot = projectRootOpt.get();
        File targetDir = new File(projectRoot, "src/test/resources/parser");

        try {
            /**
             * Check if directory exists and is actually a directory
             */
            if (!targetDir.exists() || !targetDir.isDirectory()) {
                System.out.println(
                    "Cannot find specified directory: " + targetDir.getAbsolutePath());
                return 1;
            }

            /**
             * List files with .yaml or .yml extensions only
             * Using lambda for file filtering
             */
            File[] files = targetDir.listFiles((dir, name) ->
                                                   name.toLowerCase().endsWith(".yaml") ||
                                                       name.toLowerCase().endsWith(".yml")
            );

            /**
             * Check if any YAML files were found
             * Return 0 even if no files found (not an error condition)
             */
            if (files == null || files.length == 0) {
                System.out.println("No Scenario Test files found.");
                System.out.println("Search location: " + targetDir.getAbsolutePath());
                return 0;
            }

            System.out.println(
                "\n<========= YAML Files List (" + files.length + " files) =========>");

            /**
             * Iterate through found files and print their names
             * Only prints file names, not full paths for better readability
             */
            for (File file : files) {
                System.out.println(file.getName());
            }

            return 0;

        } catch (Exception e) {
            /**
             * Handle any exceptions that occur during execution
             * Print stack trace for debugging purposes
             */
            System.err.println("Error occurred while searching files: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
