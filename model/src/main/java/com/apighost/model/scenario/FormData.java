package com.apighost.model.scenario;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.Map;

/**
 * When sending a request, the format when the type is formdata
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = FormData.Builder.class)
public class FormData {

    private Map<String, String> file;
    private Map<String, String> text;

    private FormData(Builder builder) {
        this.file = builder.file;
        this.text = builder.text;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private Map<String, String> file;
        private Map<String, String> text;

        public Builder file(Map<String, String> file) {
            this.file = file;
            return this;
        }

        public Builder text(Map<String, String> text) {
            this.text = text;
            return this;
        }

        public FormData build() {
            return new FormData(this);
        }
    }

    /**
     * Getter
     */
    public Map<String, String> getFile() {
        return file;
    }

    public Map<String, String> getText() {
        return text;
    }

}
