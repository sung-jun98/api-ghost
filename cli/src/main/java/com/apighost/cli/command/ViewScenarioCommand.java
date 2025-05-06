package com.apighost.cli.command;

import com.apighost.cli.util.ConsoleOutput;
import com.apighost.cli.util.FileUtil;
import java.io.File;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * A command that reads the current YAML scenario files. The result is displayed in the user's
 * console window. User can read/write files by notepad(Window) or vi Editor(UNIX/MacBook)
 *
 * <p>
 * Example Usage : `apighost edit exam1.yaml`
 * </p>
 *
 * @author sun-jun98
 * @version BETA-0.0.1
 */
@Command(
    name = "edit",
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

    @Option(names = {"-c", "--create"}, description = "Create a new file if none exists")
    private boolean createIfNone;

    /**
     * Opens the specified YAML file in vi editor.
     *
     * @return Integer If 0 is success, 1 if it fails
     * @throws Exception
     */
    @Override
    public Integer call() throws Exception {
        FileUtil fileUtil = new FileUtil();

        File targetDir = fileUtil.findDirectory(FileType.SCENARIO.toString());
        File targetFile = new File(targetDir, fileName);

        if (!targetFile.exists() || !targetFile.isFile()) {

            if (createIfNone) {
                boolean isSuccess = targetFile.createNewFile();
                if (!isSuccess) {
                    ConsoleOutput.printError("file create fail");
                    return 1;
                }
            } else {
                ConsoleOutput.print("Use --create option to create a new file");
                return 0;
            }
        }

        return fileUtil.openInEditor(targetFile);
    }
}
