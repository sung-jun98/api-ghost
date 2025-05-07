package com.apighost.parser.template;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Replaces placeholders in the format ${variable} with actual values from a map.
 * <p>
 * Example: template: "Hello, ${name}!" variables: { "name" -> "Alice" } result: "Hello, Alice!"
 *
 * @author haazz
 * @version 1.0
 */
public class TemplateConvertor {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([^}]+)}");

    /**
     * Replaces all ${key} placeholders in the template with the corresponding value from the map.
     *
     * @param template  the input string containing placeholders
     * @param variables a map containing variable names and their values
     * @return the substituted string
     */
    public static String convert(String template, Map<String, Object> variables) {
        if (template == null) {
            return template;
        }

        Matcher matcher = VARIABLE_PATTERN.matcher(template);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String key = matcher.group(1);
            String convertValue = "";

            if (variables != null && variables.containsKey(key)) {
                Object value = variables.get(key);
                if (value instanceof String str) {
                    convertValue = str.trim();
                } else if (value instanceof Number) {
                    convertValue = String.valueOf(value);
                }
            }
            matcher.appendReplacement(result, Matcher.quoteReplacement(convertValue));
        }

        matcher.appendTail(result);
        return result.toString();
    }
}
