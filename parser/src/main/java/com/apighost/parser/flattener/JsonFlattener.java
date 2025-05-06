package com.apighost.parser.flattener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

/**
 * Implementation of {@link Flattener} for JSON data.
 * <p>
 * Converts nested JSON objects and arrays into a flat key-value map using dot and bracket notation
 * (e.g., "user.name", "roles[0]").
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class JsonFlattener implements Flattener {

    private ObjectMapper objectMapper;

    public JsonFlattener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Flattens a JSON string into a map with dot/bracket notation keys.
     *
     * @param data the raw JSON string
     * @return a map of flattened keys and corresponding values
     * @throws RuntimeException if the input is not valid JSON
     */
    @Override
    public Map<String, Object> flatten(String data) {
        Map<String, Object> result = new LinkedHashMap<>();
        try {
            JsonNode root = objectMapper.readTree(data);
            traverse(root, "", result);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON data", e);
        }
        return result;
    }

    private void traverse(JsonNode node, String path, Map<String, Object> result) {
        if (node.isObject()) {
            node.fieldNames().forEachRemaining(field -> {
                JsonNode child = node.get(field);
                String newPath = path.isEmpty() ? field : path + "." + field;
                traverse(child, newPath, result);
            });
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                JsonNode child = node.get(i);
                String newPath = path + "[" + i + "]";
                traverse(child, newPath, result);
            }
        } else if (node.isValueNode()) {
            Object value = extractValue(node);
            result.put(path, value);
        }
    }

    private Object extractValue(JsonNode node) {
        if (node.isInt()) {
            return node.intValue();
        }
        if (node.isLong()) {
            return node.longValue();
        }
        if (node.isDouble()) {
            return node.doubleValue();
        }
        if (node.isBoolean()) {
            return node.booleanValue();
        }
        if (node.isNull()) {
            return null;
        }
        return node.asText();
    }
}

