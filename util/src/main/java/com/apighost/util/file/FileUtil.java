package com.apighost.util.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for file and directory management used in API Ghost.
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class FileUtil {

    /**
     * Finds or creates a directory based on the provided {@link FileType} and base path.
     * <p>
     * - If {@code basePath} is {@code null}, the default path {@code .apighost} in the current working directory is used. <br>
     * - If the directory doesn't exist, it is created. <br>
     * - If the path exists but is a file, a {@link RuntimeException} is thrown. <br>
     * - For {@code FileType.CONFIG}, the root of the {@code .apighost} directory is returned. <br>
     * - For other file types, subdirectories under {@code .apighost} are created and returned.
     * </p>
     *
     * @param fileType the type of file category (e.g., CONFIG, SCENARIO, RESULT)
     * @param basePath the root path under which to resolve the directory; can be {@code null}
     * @return a {@link Path} representing the directory for the specified {@code FileType}
     * @throws RuntimeException if a file already exists at the target directory location, or if directory creation fails
     */
    public static Path findDirectory(FileType fileType, Path basePath) {
        if (basePath == null) {
            basePath = Paths.get(".apighost");
        }
        if (!basePath.endsWith(".apighost")) {
            basePath = basePath.resolve(".apighost");
        }

        if (Files.exists(basePath) && !Files.isDirectory(basePath)) {
            throw new RuntimeException("Already file exist: " + basePath);
        }
        if (!Files.isDirectory(basePath)) {
            try {
                Files.createDirectories(basePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory: " + basePath, e);
            }
        }

        if (fileType == FileType.CONFIG) {
            return basePath;
        }

        Path fileDirectoryPath = basePath.resolve(fileType.name());

        /** If there is no directory, it is new. */
        if (!Files.exists(fileDirectoryPath)) {
            try {
                Files.createDirectories(fileDirectoryPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory: " + fileDirectoryPath, e);
            }
        }

        return fileDirectoryPath;
    }

    public static String inferContentType(String fileName) {
        if (fileName == null) {
            return "application/octet-stream";
        }
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return switch (extension) {
            case "jpeg", "jpg" -> "image/jpeg";
            case "png" -> "image/png";
            case "pdf" -> "application/pdf";
            case "gif" -> "image/gif";
            case "txt" -> "text/plain";
            case "mp4" -> "video/mp4";
            case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "zip" -> "application/zip";
            case "mp3" -> "audio/mpeg";
            default -> "application/octet-stream";
        };
    }
}
