package com.edu.main.function.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserAppDTO implements Serializable {

    private Long id;
    private String username;
    private String password;
    private List<String> roles;
    private String email;
}
