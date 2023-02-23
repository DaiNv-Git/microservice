package com.edu.main.function.entity;

import com.edu.main.function.dto.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "grade")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Grade implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "cgpa_4")
    private Double cgpa4;
    @Column(name = "cgpa_100")
    private Double cgpa100;
    @Column(name = "credits_accumulated")
    private String creditsAccumulated;
    private String classification;
    private String username;
    @Enumerated(EnumType.STRING)
    private Semester semester;

    public Grade( Long id,Double cgpa100, String creditsAccumulated, String classification, String username, Semester semester,Double cgpa4) {
        this.cgpa100 = cgpa100;
        this.creditsAccumulated = creditsAccumulated;
        this.classification = classification;
        this.username = username;
        this.semester = semester;
        this.id = id;
        this.cgpa4=cgpa4;
    }
}
