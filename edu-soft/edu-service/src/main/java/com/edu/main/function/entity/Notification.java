package com.edu.main.function.entity;

import com.edu.main.function.dto.enums.ReferenceNotify;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
import java.util.Date;

@Entity
@Table(name = "notification")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String message;

    @Column(name = "event_date")
    private Date eventDate;

    @Enumerated(EnumType.STRING)
    private ReferenceNotify reference;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "is_important")
    private Boolean isImportant;

    @Column(name = "is_system")
    private Boolean isSystem;

    @Column(name = "is_urgent")
    private Boolean isUrgent;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_date")
    private Date modifiedDate;
}
