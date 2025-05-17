package com.apighost.cli.command;

import com.apighost.cli.util.ConsoleOutput;
import com.apighost.util.file.BasePathHolder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import com.apighost.util.file.FileType;
import com.apighost.util.file.FileUtil;

/**
 * CLI command implementation class to search and output a subway test configuration file inside the
 * computer
 * <p>usage example : `apighost ls loadtest` </p>
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@Command(
    name = "loadtest",
    description = "View LoadTest Parameter YAML file content",
    mixinStandardHelpOptions = true
)
public class ViewLoadTestParamListCommand implements Callable<Integer> {

    /**
     * 1. Find a `.apighost/loadtest` directory <br> 2. If there is no directory, it is created <br>
     * 3. If the inside of the directory is empty, the error message output <br> 4. If not empty,
     * the internal file list output <br>
     *
     * @return return code
     * @throws RuntimeException
     * @throws IOException
     */
    @Override
    public Integer call() throws RuntimeException, IOException {

        Path loadTestDirectory = FileUtil.findDirectory(FileType.LOADTEST,
            BasePathHolder.getInstance().getBasePath());

        long fileCount = Files.list(loadTestDirectory).count();

        if (fileCount == 0) {
            ConsoleOutput.printError("No Load Test files found.");
            ConsoleOutput.print(
                "Use `apighost edit --create-loadtest` command to create a new file");

            return 0;
        }

        try {
            Files.walk(loadTestDirectory)
                .filter(path -> Files.isRegularFile(path))
                .forEach(path -> ConsoleOutput.print(path.getFileName().toString()));
        } catch (IOException e) {
            throw new RuntimeException("Error occurs during file list check", e);
        }
        return 0;
    }
}
