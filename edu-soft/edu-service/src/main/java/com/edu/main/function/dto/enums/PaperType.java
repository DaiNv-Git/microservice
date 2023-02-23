package com.edu.main.function.dto.enums;

public enum PaperType {
    TRANSCRIPT("Transcript"),
    CERTIFICATE("Certificate");

    private final String name;

    PaperType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
