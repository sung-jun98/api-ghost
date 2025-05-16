package com.apighost.cli.util.console;

import java.io.PrintWriter;

/**
 * Definition of basic methods that will be used to dynamically output the results dynamically using
 * Picocli's ANSI ESCAPE
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class DynamicDisplayManager {

    private int totalLines = 0;
    private final PrintWriter writer;

    public DynamicDisplayManager() {
        this.writer = new PrintWriter(System.out, true);
    }

    /**
     * Terminal screen full initialization method
     */
    public void initialize() {
        writer.print(AnsiEscapeUtil.CLEAR_SCREEN);
        writer.print(AnsiEscapeUtil.setCursorPosition(1, 1));
        writer.flush();
        totalLines = 0;
    }

    /**
     * Print the text that was delivered as a parameter to the console window.
     *
     * @param text
     */
    public void println(String text) {
        writer.println(text);
        writer.flush();
        totalLines++;
    }

    /**
     * The method used when you want to overwrite the text on the upper part of the already written.
     *
     * @param lineNumber
     * @param text
     */
    public void updateLine(int lineNumber, String text) {
        writer.print(AnsiEscapeUtil.setCursorPosition(lineNumber, 1));
        writer.print(AnsiEscapeUtil.CLEAR_LINE);
        writer.print(text);
        writer.flush();
    }


}
