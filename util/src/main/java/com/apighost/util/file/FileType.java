package com.apighost.util.file;

/**
 * We share the same content as the Fileutil method of the CLI module. Because of the cycle
 * reference, we first copied/pasted the web module, but we plan to improve it when refactoring.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public enum FileType {
    SCENARIO,
    RESULT,
    CONFIG,
    LOADTEST,
    RESOURCES
}
