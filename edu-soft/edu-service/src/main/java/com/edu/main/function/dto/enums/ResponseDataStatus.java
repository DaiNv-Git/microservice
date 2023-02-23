package com.edu.main.function.dto.enums;

public enum ResponseDataStatus {
    SUCCESS("Success"),
    ERROR("Error");

    private final String description;

    ResponseDataStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
