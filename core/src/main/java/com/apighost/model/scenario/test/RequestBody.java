package com.apighost.model.scenario.test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

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
