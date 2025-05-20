package com.apighost.cli.util.console;

import com.apighost.cli.util.ConsoleOutput;
import java.io.PrintWriter;
import java.util.Map;

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
    private final StringBuilder buffer;

    public DynamicDisplayManager() {
        this.writer = new PrintWriter(System.out, true);
        this.buffer = new StringBuilder();
    }

    /**
     * Terminal screen full initialization method
     */
    public void initialize() {
        writer.print(AnsiEscapeUtil.CLEAR_SCREEN);
        writer.print(AnsiEscapeUtil.setCursorPosition(1, 1));

        writer.print(AnsiEscapeUtil.SAVE_CURSOR_POSITION);
        writer.flush();
        totalLines = 0;
        buffer.setLength(0);
    }

    /**
     * Print the text that was delivered as a parameter to the console window.
     *
     * @param text
     */
    public void println(String text) {
        buffer.append(text).append("\n");
        writer.println(text);
        writer.flush();
        totalLines++;
    }

    /**
     * Updates the content displayed on the screen based on the provided updates.
     * The method restores the cursor position, iterates through the existing lines
     * of content, replaces specified lines with new content, and clears and redraws
     * the corresponding lines.
     *
     * @param updates a map where the key represents the line number (1-based) to
     *                update and the value represents the new content for that line.
     */
    public void updateScreen(Map<Integer, String> updates) {
        writer.print(AnsiEscapeUtil.RESTORE_CURSOR_POSITION);
        String[] lines = buffer.toString().split("\n");

        for (int i = 0; i < lines.length; i++) {
            int lineNum = i + 1;
            String newContent = updates.getOrDefault(lineNum, lines[i]);
            writer.print(AnsiEscapeUtil.CLEAR_LINE);
            writer.println(newContent);
            lines[i] = newContent;
        }

        buffer.setLength(0);
        buffer.append(String.join("\n", lines));

        writer.flush();
    }
}
