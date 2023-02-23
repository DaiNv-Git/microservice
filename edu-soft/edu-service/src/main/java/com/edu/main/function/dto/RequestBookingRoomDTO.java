package com.edu.main.function.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class RequestBookingRoomDTO implements Serializable {

    private String description;
    private Timestamp startTime;
    private Timestamp endTime;
    private String status;

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Room name is mandatory")
    private String roomName;
}
