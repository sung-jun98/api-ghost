package com.apighost.cli.util;

import picocli.CommandLine;

/**
 * Utility class for displaying output messages to the console. Supports different message styles
 * including normal messages, bold messages, error messages, and bold error messages.
 *
 * <p>
 * <p>
 * Usage Example :<br /> ```<br /> ConsoleOutput.print("This is noramal messgae."); <br />
 * ConsoleOutput.printBold("This is important message."); <br /> ConsoleOutput.printError("An error
 * occured."); <br /> ConsoleOutput.printErrorBold("Serveral error occured"); <br /> ```<br />
 * </p>
 *
 * @author sun-jun98
 * @version BETA-0.0.1
 */
public class ConsoleOutput {

    private static final CommandLine.Help.Ansi ANSI = CommandLine.Help.Ansi.AUTO;

    /**
     * Print a normal message (default white).
     *
     * @param message Message to print
     */
    public static void print(String message) {
        System.out.println(message);
    }

    /**
     * Print the emphasized general message (bold white).
     *
     * @param message Message to print
     */
    public static void printBold(String message) {
        System.out.println(ANSI.string("@|bold " + message + "|@"));
    }

    /**
     * Print an error message (red).
     *
     * @param message Message to print
     */
    public static void printError(String message) {
        System.err.println(ANSI.string("@|red " + message + "|@"));
    }

    /**
     * Print a serious error message (bold red).
     *
     * @param message Message to print
     */
    public static void printErrorBold(String message) {
        System.err.println(ANSI.string("@|bold,red " + message + "|@"));
    }
}
