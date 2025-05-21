package com.apighost.cli.util.console;

/**
 * Util defining the escape variable mainly used in Picocli's ANSI ESCAPE
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class AnsiEscapeUtil {

    public static final String ESC = "\u001B";
    public static final String CSI = ESC + "[";

    public static final String CURSOR_POSITION = CSI + "%d;%dH";
    public static final String CLEAR_LINE = CSI + "2K";
    public static final String CLEAR_SCREEN = "\033[H\033[2J";

    public static final String SAVE_CURSOR_POSITION = CSI + "s";
    public static final String RESTORE_CURSOR_POSITION = CSI + "u";

    public static String setCursorPosition(int row, int column) {
        return String.format(CURSOR_POSITION, row, column);
    }


}
