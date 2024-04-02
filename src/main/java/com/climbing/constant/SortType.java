package com.climbing.constant;

public enum SortType {
    POPULAR("POPU"),
    LATEST("LATE"),
    DISTANCE("DIST"),
    NAME("NAME");

    public final String value;

    SortType(String value) {
        this.value = value;
    }
}
