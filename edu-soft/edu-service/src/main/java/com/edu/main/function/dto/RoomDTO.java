package com.edu.main.function.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO implements Serializable {

    @NotBlank(message = "Name is mandatory")
    private String name;
    private Boolean status;
}
