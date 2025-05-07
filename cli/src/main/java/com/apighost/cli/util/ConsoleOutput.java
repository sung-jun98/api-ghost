package com.apighost.cli.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
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

    private static final Logger logger = Logger.getLogger(ConsoleOutput.class.getName());
    private static final CommandLine.Help.Ansi ANSI = CommandLine.Help.Ansi.AUTO;

    static {
        logger.setLevel(Level.INFO);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return record.getMessage() + "\n";
            }
        });
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
    }

    /**
     * Print a normal message (default white).
     *
     * @param message Message to print
     */
    public static void print(String message) {
        logger.info(message);
    }

    /**
     * Print the emphasized general message (thick white).
     *
     * @param message Message to print
     */
    public static void printBold(String message) {
        logger.info(ANSI.string("@|bold " + message + "|@"));
    }

    /**
     * Print an error message (red).
     *
     * @param message Message to print
     */
    public static void printError(String message) {
        logger.severe(ANSI.string("@|red " + message + "|@"));
    }

    /**
     * Print a serious error message (coarse red).
     *
     * @param message Message to print
     */
    public static void printErrorBold(String message) {
        logger.severe(ANSI.string("@|bold,red " + message + "|@"));
    }


}
