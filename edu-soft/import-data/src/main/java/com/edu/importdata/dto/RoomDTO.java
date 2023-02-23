package com.edu.importdata.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RoomDTO implements Serializable {

    private String name;
}
