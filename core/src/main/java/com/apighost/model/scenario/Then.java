package com.apighost.model.scenario;

import java.util.Map;

public class Then {

    private Map<String, String> save;
    private String next;

    public Map<String, String> getSave() {
        return save;
    }

    public void setSave(Map<String, String> save) {
        this.save = save;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
