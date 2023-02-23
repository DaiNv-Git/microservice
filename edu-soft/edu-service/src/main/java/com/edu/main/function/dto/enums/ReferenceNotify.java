package com.edu.main.function.dto.enums;

public enum ReferenceNotify {
    ALL(0L, "Notify all"),
    IT069IU(1L, "Notify for Object-Oriented Programming subject"),
    IT153IU(2L, "Notify for Discrete Mathematics subject"),
    IT072IU(3L, "Notify for Data Structures and Algorithims subject"),
    IT091IU(4L, "Notify for Computere Network subject"),
    IT089IU(4L, "Notify for Computer Architecture subject");

    private final Long id;
    private final String description;

    ReferenceNotify(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }
}
