package com.apighost.cli.command;

import com.apighost.cli.util.ConsoleOutput;
import com.apighost.util.file.BasePathHolder;
import com.apighost.util.file.FileType;
import com.apighost.util.file.FileUtil;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * Command to delete a file
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@Command(
    name = "rm",
    description = "Delete file",
    mixinStandardHelpOptions = true
)
public class DeleteLoadTestParameterCommand implements Callable<Integer> {

    @Parameters(
        index = "0",
        description = "The name of the file to remove",
        arity = "1"
    )
    private String fileName;
    private boolean fileExists;
    private Path filePath;
    private Path loadTestDirectory;

    @Override
    public Integer call() throws Exception {
        /**
         *  Inquiry whether there is a file that matches the filename
         *  while traveling around the folder `.apighost/LOADTEST`
         */
        loadTestDirectory = FileUtil.findDirectory(FileType.SCENARIO,
            BasePathHolder.getInstance().getBasePath());

        filePath = loadTestDirectory.resolve(fileName);
        fileExists = Files.exists(filePath) && Files.isRegularFile(filePath);

        if (fileExists) {
            Files.delete(filePath);
            ConsoleOutput.print("File " + fileName + " deleted successfully");
            fileExists = false;
        }

        /**
         *  Inquiry whether there is a file that matches the filename
         *  while traveling around the folder `.apighost/LOADTEST`
         */
        loadTestDirectory = FileUtil.findDirectory(FileType.LOADTEST,
            BasePathHolder.getInstance().getBasePath());

        filePath = loadTestDirectory.resolve(fileName);
        fileExists = Files.exists(filePath) && Files.isRegularFile(filePath);

        /**
         * Delete if it exists
         */
        if (fileExists) {
            Files.delete(filePath);
            ConsoleOutput.print("File " + fileName + " deleted successfully");
            fileExists = false;
        }

        return 0;
    }
}
