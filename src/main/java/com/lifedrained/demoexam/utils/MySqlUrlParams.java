package com.lifedrained.demoexam.utils;

public enum MySqlUrlParams {

    ALLOW_INFILE("allowLoadLocalInfile=true"), CREATE_DB("createDatabaseIfNotExist=true");

    private final String value;

    MySqlUrlParams(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
