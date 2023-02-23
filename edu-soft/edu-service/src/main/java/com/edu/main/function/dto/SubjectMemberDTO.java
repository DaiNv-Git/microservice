package com.edu.main.function.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SubjectMemberDTO implements Serializable {

    private Long subjectId;
    private String username;
}
