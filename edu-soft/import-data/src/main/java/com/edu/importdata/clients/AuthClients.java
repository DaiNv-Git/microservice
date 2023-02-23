package com.edu.importdata.clients;

import com.edu.importdata.config.FeignClientInterceptor;
import com.edu.importdata.dto.UserAppDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "AUTH-SERVICE", configuration = FeignClientInterceptor.class)
public interface AuthClients {

    @PostMapping("/api/v1/import/users")
    void importUser(@RequestBody List<UserAppDTO> userAppDTOS);

    @GetMapping("/api/v1/import/welcome")
    void testConnection();

    @GetMapping("/api/v1/users")
    EntityModel<UserAppDTO> getCurrentUser();
}
