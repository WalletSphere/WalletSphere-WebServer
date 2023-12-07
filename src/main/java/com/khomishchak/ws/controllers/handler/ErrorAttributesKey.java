package com.khomishchak.ws.controllers.handler;

public enum ErrorAttributesKey {
    CODE("code"),
    MESSAGE("message");

    private final String value;

    ErrorAttributesKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
