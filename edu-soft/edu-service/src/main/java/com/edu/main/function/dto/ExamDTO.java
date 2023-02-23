package com.edu.main.function.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamDTO implements Serializable {

    private Long id;
    private String subjectCode;
    private String subjectName;
    private Date time;
    private String roomName;
}
