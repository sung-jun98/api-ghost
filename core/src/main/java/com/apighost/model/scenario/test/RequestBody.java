package com.apighost.model.scenario.test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Represents the request body structure for an HTTP request. It contains either form data or a JSON
 * string to be sent in the request. Formdata and JSON String cannot come at the same time. Only one
 * of the two must come, and then the other is NULL.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
@JsonDeserialize(builder = RequestBody.Builder.class)
public class RequestBody {

    private FormData formdata;
    private String json;

    private RequestBody(Builder builder) {
        this.formdata = builder.formdata;
        this.json = builder.json;
    }

    /**
     * Builder
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private FormData formdata;
        private String json;

        public Builder formdata(FormData formdata) {
            this.formdata = formdata;
            return this;
        }

        public Builder json(String json) {
            this.json = json;
            return this;
        }

        public RequestBody build() {
            return new RequestBody(this);
        }
    }

    /**
     * Getter
     */
    public FormData getFormdata() {
        return formdata;
    }

    public String getJson() {
        return json;
    }
}
