package com.edu.main.function.dto.enums;

public enum RequestStatus {
    PENDING("Pending"),
    DONE("Done"),
    REJECTED("Rejected"),
        UNPAID("Unpaid");

    private final String name;

    RequestStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
