package com.apighost.model.scenario.result;

import java.util.Map;

/**
 * Defines the action to take when a branch condition is met. This may include saving variables and
 * specifying the next scenario step.
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class ThenAction {

    private final Map<String, Object> save;
    private final String next;

    private ThenAction(Builder builder) {
        this.save = builder.save;
        this.next = builder.next;
    }

    /**
     * Builder
     */
    public static class Builder {

        private Map<String, Object> save;
        private String next;

        public Builder save(Map<String, Object> save) {
            this.save = save;
            return this;
        }

        public Builder next(String next) {
            this.next = next;
            return this;
        }

        public ThenAction build() {
            return new ThenAction(this);
        }
    }

    /**
     * Getter
     */
    public Map<String, Object> getSave() {
        return save;
    }

    public String getNext() {
        return next;
    }
}
