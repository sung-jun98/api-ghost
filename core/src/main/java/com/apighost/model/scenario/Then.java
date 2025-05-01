package com.apighost.model.scenario;

import java.util.Map;

public class Then {

    private Map<String, String> save;
    private String next;

    private Then(Builder builder) {
        this.save = builder.save;
        this.next = builder.next;
    }

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

    public Map<String, String> getSave() {
        return save;
    }

    public String getNext() {
        return next;
    }
}
