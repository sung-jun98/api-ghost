package com.apighost.cli.util;

import com.apighost.cli.command.FileType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Utility class for file and path operations.
 *
 * @author sun-jun98
 * @version BETA-0.0.1
 */
public class FileUtil {

    /**
     * Finds or creates a directory under the user's home directory for a specified file type. If
     * the directory does not exist, it is created.
     *
     * @param fileType The type of file or the name of the subdirectory to locate or create.
     * @return A File object representing the found or newly created directory.
     * @throws RuntimeException If an error occurs while creating the directory.
     */
    public static File findDirectory(FileType fileType) {

        // Create a path based on the user's home directory
        String userHome = System.getProperty("user.home");
        Path startPoint = Paths.get(userHome, ".apighost");
        Path typeDir = startPoint.resolve(fileType.name());

        /** If there is no directory, it is new. */
        if (!Files.exists(typeDir)) {
            try {
                Files.createDirectories(typeDir);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory: " + typeDir, e);
            }
        }

        return typeDir.toFile();
    }


    /**
     * Opens the given file in the default text editor based on the operating system. For Windows,
     * it uses Notepad. For Unix/Linux/Mac systems, it uses Vi. Returns the exit code of the editor
     * process.
     *
     * @param file The file to be opened in the editor.
     * @return An integer representing the editor process's exit code (0 for success, non-zero for
     * error).
     * @throws InterruptedException If there is an issue starting the editor process or accessing
     *                              the file.
     */
    public int openInEditor(File file) throws InterruptedException, IOException {

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
        } catch (InterruptedException | IOException e) {
            System.err.println("Error opening file in editor: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * Removes illegal characters from a file name to ensure compatibility across all major
     * operating systems.
     * <p>
     * The method removes the following characters which are not allowed in file names on most
     * systems:
     * <ul>
     *     <li>Windows: <code>\ / : * ? " &lt; &gt; |</code></li>
     *     <li>Control characters (Unicode category Cntrl)</li>
     * </ul>
     * This helps prevent issues when saving files with user-defined names.
     * </p>
     *
     * @param fileName the original file name that may contain illegal characters
     * @return a sanitized file name with all illegal characters removed
     */
    public static String replaceIllegalFileName(String fileName) {
        return fileName.replaceAll("[\\\\/:*?\"<>|\\p{Cntrl}]", "");
    }
}
