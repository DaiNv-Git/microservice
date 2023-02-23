package com.edu.importdata.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class SubjectMemberDTO implements Serializable {

    private Long subjectId;
    private String username;
}
