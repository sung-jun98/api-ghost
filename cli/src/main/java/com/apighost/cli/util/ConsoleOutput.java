package com.apighost.cli.util;
import picocli.CommandLine;

/**
 * Utility class for displaying output messages to the console.
 * Supports different message styles including normal messages,
 * bold messages, error messages, and bold error messages.
 *
 * <p>
 *
 *      Usage Example :<br />
 *```<br />
 *      ConsoleOutput.print("This is noramal messgae."); <br />
 *      ConsoleOutput.printBold("This is important message."); <br />
 *      ConsoleOutput.printError("An error occured."); <br />
 *      ConsoleOutput.printErrorBold("Serveral error occured"); <br />
 *```<br />
 * </p>
 *
 * @author sun-jun98
 * @version BETA-0.0.1
 */
public class ConsoleOutput {
    /**
     * Print a normal message (default white).
     *
     * @param message Message to print
     */
    public static void print(String message) {
        System.out.println(message);
    }

    /**
     * Print the emphasized general message (thick white).
     *
     * @param message Message to print
     */
    public static void printBold(String message) {
        String coloredMessage = CommandLine.Help.Ansi.AUTO.string("@|bold " + message + "|@");
        System.out.println(coloredMessage);
    }

    /**
     * Print an error message (red).
     *
     * @param message Message to print
     */
    public static void printError(String message) {
        String coloredMessage = CommandLine.Help.Ansi.AUTO.string("@|red " + message + "|@");
        System.err.println(coloredMessage);
    }

    /**
     * Print a serious error message (coarse red).
     *
     * @param message Message to print
     */
    public static void printErrorBold(String message) {
        String coloredMessage = CommandLine.Help.Ansi.AUTO.string("@|bold,red " + message + "|@");
        System.err.println(coloredMessage);
    }


}
