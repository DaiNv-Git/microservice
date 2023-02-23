package com.edu.main.function.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
public class NotificationDTO implements Serializable {

    private Long id;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Message is mandatory")
    private String message;
    private Boolean isRead;
    private Date createdDate;
    private Date modifiedDate;
    private Long subjectId;
    private String subjectName;
    private Boolean isImportant;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
    private Date eventDate;
    private Boolean isUrgent;
}
