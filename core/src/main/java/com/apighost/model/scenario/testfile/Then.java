package com.apighost.model.scenario.testfile;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.Map;

/**
 * Define conditions that will happen after satisfying the conditions
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = Then.Builder.class)
public class Then {

    private Map<String, String> save;
    private String next;


    private Then(Builder builder) {
        this.save = builder.save;
        this.next = builder.next;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private Map<String, String> save;
        private String next;

        public Builder save(Map<String, String> save) {
            this.save = save;
            return this;
        }

        public Builder next(String next) {
            this.next = next;
            return this;
        }

        public Then build() {
            return new Then(this);
        }
    }

    /**
     * Getter
     */
    public Map<String, String> getSave() {
        return save;
    }

    public String getNext() {
        return next;
    }
}
