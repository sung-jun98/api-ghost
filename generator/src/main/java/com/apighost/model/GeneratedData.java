package com.apighost.model;


/**
 * Represents a piece of generated data with a name and its corresponding value.
 * <p>
 * This model is commonly used to hold key-value pairs generated through
 * the OpenAI-based data generation process.
 * </p>
 *
 * @author kobenlys
 * @version BETA-0.0.1
 */
public class GeneratedData {

    private String name;
    private Object value;

    public GeneratedData(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
