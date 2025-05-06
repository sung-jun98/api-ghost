package com.apighost.parser.flattener;

import java.util.Map;

/**
 * Interface for flattening structured data (e.g., JSON, XML, form-data, ...) into a key-value map
 * using dot and/or bracket notation.
 * <p>
 * This is used to convert hierarchical data into a flat structure for comparison, validation, or
 * inspection purposes.
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public interface Flattener {

    /**
     * Flattens the structured data into a key-value map where keys use dot/bracket notation.
     *
     * @param data the raw structured data as a string
     * @return a {@code Map<String, Object>} representing the flattened structure
     * @throws RuntimeException if parsing or flattening fails
     */
    Map<String, Object> flatten(String data);
}
