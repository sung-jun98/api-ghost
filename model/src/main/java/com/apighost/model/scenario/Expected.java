package com.apighost.model.scenario;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.Map;

/**
 * Conditions of quarterly inquiries
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = Expected.Builder.class)
public class Expected {

    private String status;
    private Map<String, Object> value;


    private Expected(Builder builder) {
        this.status = builder.status;
        this.value = builder.value;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private String status;
        private Map<String, Object> value;

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder value(Map<String, Object> value) {
            this.value = value;
            return this;
        }

        public Expected build() {
            return new Expected(this);
        }
    }

    /**
     * Getter
     */
    public String getStatus() {
        return status;
    }

    public Map<String, Object> getValue() {
        return value;
    }

}
