package com.edu.importdata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAppDTO implements Serializable {

    private Long id;
    private String username;
    private String password;
    private List<String> roles;
    private String email;
}
