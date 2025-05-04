package com.apighost.cli.command;

import java.io.File;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;

/**
 * A command that reads the scenario resulls files. The result is displayed in the user's console
 * window.
 * <p>
 * Example Usage : `apighost view-results`
 *
 * @author sun-jun98
 * @version BETA-0.0.1
 */
@Command(
    name = "view-results",
    description = "View result JSON file list content",
    mixinStandardHelpOptions = true
)
public class ViewResultListCommand implements Callable<Integer> {

    /**
     * Find the project root directory by looking for build.gradle A method for finding the root of
     * the user environment
     *
     * @param currentDir starting directory
     * @return File object pointing to project root, or null if not found
     */
    private File findProjectRoot(File currentDir) {
        File file = currentDir;
        while (file != null) {
            if (new File(file, "build.gradle").exists()) {
                return file;
            }
            file = file.getParentFile();
        }
        return null;
    }

    /**
     * In the user's specific local directory, only json files are found and the list is shown in
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
        File projectRoot = findProjectRoot(new File(currentDir));
        if (projectRoot == null) {
            System.out.println("Unable to find project root directory");
            return 1;
        }

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
             * List files with .json extensions only
             * Using lambda for file filtering
             */
            File[] files = targetDir.listFiles((dir, name) ->
                                                   name.toLowerCase().endsWith(".json")
            );

            /**
             * Check if any json files were found
             * Return 0 even if no files found (not an error condition)
             */
            if (files == null || files.length == 0) {
                System.out.println("No Scenario Test files found.");
                System.out.println("Search location: " + targetDir.getAbsolutePath());
                return 0;
            }

            System.out.println(
                "\n<========= Test Result Files List (" + files.length + " files) =========>");

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
