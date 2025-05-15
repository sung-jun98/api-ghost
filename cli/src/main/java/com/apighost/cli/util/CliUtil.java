package com.apighost.cli.util;

import java.io.IOException;
import java.nio.file.Path;

/**
 * A class that collects utility -related methods used only inside the CLI module. Things used in
 * two or more modules are missed into a common UTIL module. *
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class CliUtil {

    /**
     * Opens the given file in the default text editor based on the operating system. For Windows,
     * it uses Notepad. For Unix/Linux/Mac systems, it uses Vi. Returns the exit code of the editor
     * process.
     *
     * @param path The file to be opened in the editor.
     * @return An integer representing the editor process's exit code (0 for success, non-zero for
     * error).
     * @throws InterruptedException If there is an issue starting the editor process or accessing
     *                              the file.
     */
    public int openInEditor(Path path) throws InterruptedException, IOException {

        try {
            /** Determine the operating system to choose the right command */
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder processBuilder;

            if (os.contains("win")) {
                processBuilder = new ProcessBuilder("notepad.exe",
                    path.toAbsolutePath().toString());
            } else {
                processBuilder = new ProcessBuilder("vi", path.toAbsolutePath().toString());
            }

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                System.out.println("Editor exited with code: " + exitCode);
                return exitCode;
            }

            return 0;
        } catch (InterruptedException | IOException e) {
            System.err.println("Error opening file in editor: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
