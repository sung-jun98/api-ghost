package com.apighost.model.collector;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents metadata for a field, including its type, name, and nested fields if applicable.
 *
 * <p>This class is useful for describing structured data such as objects or DTOs with nested
 * properties.</p>
 *
 * @author oneweeek
 * @version BETA-0.0.1
 */
public class FieldMeta {

    private String type;
    private String name;
    private List<FieldMeta> nestedFields;

    public FieldMeta() {

    }

    /**
     * Constructs a {@code FieldMeta} with the specified type and name. Initializes an empty list
     * for nested fields.
     *
     * @param type the field type
     * @param name the field name
     */
    public FieldMeta(String type, String name) {
        this.type = type;
        this.name = name;
        this.nestedFields = new ArrayList<>();
    }

    /**
     * Constructs a {@code FieldMeta} with the specified name, type, and nested fields.
     *
     * @param name         the field name
     * @param type         the field type
     * @param nestedFields the list of nested field metadata
     */
    public FieldMeta(String name, String type, List<FieldMeta> nestedFields) {
        this.name = name;
        this.type = type;
        this.nestedFields = nestedFields != null ? nestedFields : new ArrayList<>();
    }

    /**
     * Returns the field name.
     *
     * @return the name as a string
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the field type.
     *
     * @return the type as a string
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the list of nested field metadata.
     *
     * @return the list of nested fields
     */
    public List<FieldMeta> getNestedFields() {
        return nestedFields;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNestedFields(List<FieldMeta> nestedFields) {
        this.nestedFields = nestedFields;
    }
}
