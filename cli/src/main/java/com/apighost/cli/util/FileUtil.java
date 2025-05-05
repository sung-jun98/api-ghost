package com.apighost.cli.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;


/**
 * Utility class for file and path operations.
 *
 * @author sun-jun98
 * @version BETA-0.0.1
 */
public class FileUtil {

    /**
     * Find the project root directory by looking for build.gradle A method for finding the root of
     * the user environment
     *
     * @param currentDir starting directory
     * @return Optional When the optional directory exists, the directory is returned to the path,
     * and if it is not, it returns the empty optional object
     */
    public static Optional<File> findProjectRoot(File currentDir) {
        File file = currentDir;
        while (file != null) {
            if (new File(file, "build.gradle").exists()) {
                return Optional.of(file);
            }
            file = file.getParentFile();
        }
        return Optional.empty();
    }

    /**
     * Prompts the user to create a new file and opens it in an editor.
     *
     * @param targetDir Target directory
     * @return Exit code (0 for success, non-zero for error)
     */
    public int createNewFile(File targetDir, String fileName) throws IOException {

        try {
            File newFile = new File(targetDir, fileName);
            /** Create new without files */
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            int exitCode = this.openInEditor(newFile);

            if (exitCode == 0) {
                System.out.println("File created and edited successfully.");
                return 0;
            } else {
                System.out.println("Editor exited with an error.");
                return 1;
            }
        } catch (Exception e) {
            System.err.println("Error creating or opening file: " + e.getMessage());
            return 1;
        }
    }


    /**
     * Opens the given file in the default text editor based on the operating system. For Windows,
     * it uses Notepad. For Unix/Linux/Mac systems, it uses Vi. Returns the exit code of the editor
     * process.
     *
     * @param file The file to be opened in the editor.
     * @return An integer representing the editor process's exit code (0 for success, non-zero for
     * error).
     * @throws IOException If there is an issue starting the editor process or accessing the file.
     */
    public int openInEditor(File file) throws IOException {

        try {
            /** Determine the operating system to choose the right command */
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder processBuilder;

            if (os.contains("win")) {
                processBuilder = new ProcessBuilder("notepad.exe", file.getAbsolutePath());
            } else {
                processBuilder = new ProcessBuilder("vi", file.getAbsolutePath());
            }

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
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
