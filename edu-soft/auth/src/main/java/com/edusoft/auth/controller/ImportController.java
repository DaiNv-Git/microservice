package com.edusoft.auth.controller;

import com.edusoft.auth.dto.UserAppDTO;
import com.edusoft.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/import")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ImportController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> testConnection() {
        System.out.println("Connect successful");
        return new ResponseEntity<>("Connect with ImportController successful..", HttpStatus.OK);
    }

    @PostMapping("/users")
    public void importUser(@RequestBody List<UserAppDTO> userAppDTOS) {
        userService.importUser(userAppDTOS);
    }
}
