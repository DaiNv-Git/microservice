package com.edu.main.function.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class RequestPaperDTO implements Serializable {

    private Long id;

    @NotBlank(message = "Paper type is mandatory")
    private String type;

    private String username;
    private String status;
}
