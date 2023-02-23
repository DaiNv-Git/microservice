package com.edu.main.function.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeTableDTO implements Serializable {

    private String subjectName;
    private String roomName;
    private String startTime;
    private String endTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Ho_Chi_Minh")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Ho_Chi_Minh")
    private Date endDate;
}
