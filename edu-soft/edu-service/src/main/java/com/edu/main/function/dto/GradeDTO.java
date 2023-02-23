package com.edu.main.function.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradeDTO implements Serializable {
    private Long id;
    private String username;
    private Double cgpa100;
    private Double cgpa4;
    private String creditsAccumulated;
    private String classification;
    private String semester;
    public GradeDTO(String username, Double cgpa100, String creditsAccumulated, String classification, String semester,Double cgpa4) {
        this.username = username;
        this.cgpa100 = cgpa100;
        this.creditsAccumulated = creditsAccumulated;
        this.classification = classification;
        this.semester = semester;
        this.cgpa4=cgpa4;
    }
}
