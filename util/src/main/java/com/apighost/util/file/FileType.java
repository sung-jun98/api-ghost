package com.apighost.util.file;

/**
 * We share the same content as the Fileutil method of the CLI module. Because of the cycle
 * reference, we first copied/pasted the web module, but we plan to improve it when refactoring.
 *
<<<<<<< Updated upstream:web/src/main/java/com/apighost/web/util/FileType.java
 * @author sun-jun98
=======
 * @author sung-jun98
>>>>>>> Stashed changes:util/src/main/java/com/apighost/util/file/FileType.java
 * @version BETA-0.0.1
 */
public enum FileType {
    SCENARIO,
    RESULT,
    CONFIG
}
