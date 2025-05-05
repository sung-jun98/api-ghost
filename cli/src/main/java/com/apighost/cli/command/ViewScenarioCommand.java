package com.apighost.cli.command;

import com.apighost.cli.util.FileUtil;
import java.io.File;
import java.util.Optional;
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
        FileUtil fileUtil = new FileUtil();
        /** Get current working directory */
        String currentDir = System.getProperty("user.dir");

        Optional<File> projectRootOpt = fileUtil.findProjectRoot(new File(currentDir));
        if (projectRootOpt.isEmpty()) {
            System.out.println("Unable to find project root directory");
            return 1;
        }

        File projectRoot = projectRootOpt.get();
        File targetDir = new File(projectRoot, "src/test/resources/parser");
        File targetFile = new File(targetDir, fileName);

        /** Check if the file exists */
        if (!targetFile.exists() || !targetFile.isFile()) {

            if (createIfNone) {
                return fileUtil.createNewFile(targetDir, fileName);
            } else {
                System.out.println("Use --create option to create a new file");
                return 0;
            }
        }

        /** Check if the file is readable */
        if (!targetFile.canRead()) {
            System.out.println("Cannot read file: " + targetFile.getAbsolutePath());
            return 1;
        }

        return fileUtil.openInEditor(targetFile);

    }

}
