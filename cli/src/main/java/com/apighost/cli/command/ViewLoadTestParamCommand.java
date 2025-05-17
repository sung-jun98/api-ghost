package com.apighost.cli.command;

import com.apighost.cli.util.ConsoleOutput;
import com.apighost.util.file.BasePathHolder;
import com.apighost.util.file.FileType;
import com.apighost.util.file.FileUtil;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import com.apighost.cli.util.CliUtil;

/**
 * Command implementation class that modifies and generates a subway test setting file
 *
 * <p>
 * `apighost edit-load fileName.yaml` <br> `apighost edit-load --create fileName.yaml` <br>
 * `apighost edit-load -c fileName.yaml`
 * </p>
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@Command(
    name = "edit-load",
    description = "Open a YAML load test parameter file in vi editor",
    mixinStandardHelpOptions = true
)
public class ViewLoadTestParamCommand implements Callable<Integer> {

    @Parameters(
        index = "0",
        description = "The name of the load test parameter YAML file to edit",
        arity = "1"
    )
    private String fileName;

    @Option(names = {"-c", "--create"}, description = "Create a new file if none exists")
    private boolean createIfNone;

    /**
     * Opens the specified YAML file in vi editor(Linux, mac) or notePad(window)
     *
     * @return Integer If 0 is success, 1 if it fails
     * @throws Exception
     */
    @Override
    public Integer call() throws Exception {
        CliUtil cliUtil = new CliUtil();

        Path loadTestDirectory = FileUtil.findDirectory(FileType.LOADTEST,
            BasePathHolder.getInstance().getBasePath());

        Path filePath = loadTestDirectory.resolve(fileName);
        boolean fileExists = Files.exists(filePath) && Files.isRegularFile(filePath);

        if (!fileExists) {
            if (createIfNone) {
                Path newPath = Files.createFile(filePath);
                cliUtil.openInEditor(newPath);
            }

            if (!createIfNone) {
                ConsoleOutput.printError(
                    "File not found.\n Please use --create option to create a new file");
            }
        }

        if (fileExists) {
            cliUtil.openInEditor(filePath);
            return 0;
        }

        return 0;
    }
}
