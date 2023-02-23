package com.edu.importdata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GradeDTO implements Serializable {
    private Long id;
    private String username;
    private Double cgpa100;
    private String creditsAccumulated;
    private String classification;
    private Double advancedMath ;
    private Double probabilityStatistics;
    private Double algorithms;
    private String semester;

    public GradeDTO(String username, Double cgpa100, String creditsAccumulated, String classification, Double advancedMath, Double probabilityStatistics, Double algorithms, String semester) {
        this.username = username;
        this.cgpa100 = cgpa100;
        this.creditsAccumulated = creditsAccumulated;
        this.classification = classification;
        this.advancedMath = advancedMath;
        this.probabilityStatistics = probabilityStatistics;
        this.algorithms = algorithms;
        this.semester = semester;
    }
}
