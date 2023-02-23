package com.edu.main.function.dto.enums;

public enum Semester {
    SEM_2_2("2nd semester 2nd year"),
    SEM_1_3("1st semester 3rd year"),
    SEM_2_3("2nd semester 3rd year"),
    SEM_1_4("1st semester 4th year"),
    Certificate("Certificate");

    private final String name;

    Semester(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
