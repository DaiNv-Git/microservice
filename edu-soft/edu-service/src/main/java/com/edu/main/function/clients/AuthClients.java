package com.edu.main.function.clients;

import com.edu.main.function.config.FeignClientInterceptor;
import com.edu.main.function.dto.UserAppDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "AUTH-SERVICE", configuration = FeignClientInterceptor.class)
public interface AuthClients {

    @GetMapping("/api/v1/users")
    UserAppDTO getCurrentUser();

    @PostMapping("/api/v1/users/username")
    UserAppDTO getUser(@RequestBody String username);

    @PostMapping("/api/v1/users/usernames")
    List<UserAppDTO> getUserByUsernames(@RequestBody List<String> usernames);
}
