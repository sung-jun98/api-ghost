package com.apighost.cli.command;

import java.io.File;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * A command that reads the current YAML scenario files. The result is displayed in the user's
 * console window.
 * User can read/write files by notepad(Window) or vi Editor(UNIX/MacBook)
 *
 * <p>
 * Example Usage : `apighost show-scenario fileName1`
 *
 * @author sun-jun98
 * @version BETA-0.0.1
 */
@Command(
    name = "show-scenario",
    description = "Open a YAML scenario file in vi editor",
    mixinStandardHelpOptions = true
)
public class ViewScenarioCommand implements Callable<Integer> {
    @Parameters(
        index = "0",
        description = "The name of the YAML file to edit",
        arity = "1"
    )
    private String fileName;

    /**
     * Find the project root directory by looking for build.gradle
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
     * Opens the specified YAML file in vi editor.
     *
     * @return Integer If 0 is success, 1 if it fails
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

        /** Create target directory path */
        File targetDir = new File(projectRoot, "src/test/resources/parser");
        File targetFile = new File(targetDir, fileName);

        /** Check if the file exists */
        if (!targetFile.exists() || !targetFile.isFile()) {
            System.out.println("File not found: " + targetFile.getAbsolutePath());
            return 1;
        }

        /** Check if the file is readable */
        if (!targetFile.canRead()) {
            System.out.println("Cannot read file: " + targetFile.getAbsolutePath());
            return 1;
        }

        try {
            /** Determine the operating system to choose the right command */
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder processBuilder;

            if (os.contains("win")) {
                /** In Windows System, the default text editor (Notepad) */
                processBuilder = new ProcessBuilder("notepad.exe", targetFile.getAbsolutePath());
            } else {
                /** Use VI in Unix/Linux/Mac System */
                processBuilder = new ProcessBuilder("vi", targetFile.getAbsolutePath());
            }

            /** Redirect error stream to output stream */
            processBuilder.redirectErrorStream(true);

            /** Start the process */
            Process process = processBuilder.start();

            /** Wait for the process to complete */
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                System.out.println("Editor exited with code: " + exitCode);
                return exitCode;
            }

            return 0;
        } catch (Exception e) {
            System.err.println("Error opening file in editor: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }

}
