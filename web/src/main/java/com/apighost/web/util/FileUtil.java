package com.apighost.web.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * We share the same content as the Fileutil method of the CLI module. Because of the cycle
 * reference, we first copied/pasted the web module, but we plan to improve it when refactoring.
 *
 * @author sun-jun98
 * @version BETA-0.0.1
 */
public class FileUtil {

    public static File findDirectory(FileType fileType) {

        /** Create a path based on the user's home directory */
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

    public static String replaceIllegalFileName(String fileName) {
        return fileName.replaceAll("[\\\\/:*?\"<>|\\p{Cntrl}]", "");
    }
}
