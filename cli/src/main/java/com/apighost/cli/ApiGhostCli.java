package com.apighost.cli;


import com.apighost.cli.command.ExecuteScenairoCommand;
import com.apighost.cli.command.ShowGuiCommand;
import com.apighost.cli.command.ViewResultCommand;
import com.apighost.cli.command.ViewScenarioCommand;
import com.apighost.cli.command.ViewScenarioListCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * Starting point of CLIs. Because I specified the class as MainClassName in Build.gradle, When
 * `java -jar` runs Java file, the class is run first. Classes in other folders are stretched around
 * that class.
 *
 * @author sun-jun98
 * @version BETA-0.0.1
 */
@Command(
    name = "apighost",
    version = "1.0.0",
    description = "API Ghost CLI Entry point.",
    mixinStandardHelpOptions = true,
    subcommands = {
        ViewScenarioListCommand.class,
        ViewScenarioCommand.class,
        ShowGuiCommand.class,
        ViewResultCommand.class,
        ExecuteScenairoCommand.class
    }
)
public class ApiGhostCli implements Runnable {

    /**
     * The entry point of the application and the container of the lower command
     *
     * @param args Enter the args where the command you entered is here.
     */
    public static void main(String[] args) {
        int exitCode = new CommandLine(new ApiGhostCli()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {

        /** Help output if the command is not provided */
        CommandLine.usage(this, System.out);
    }
}
