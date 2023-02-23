package com.edusoft.auth.controller;

import com.edusoft.auth.dto.ChangePasswordDTO;
import com.edusoft.auth.dto.ResponseData;
import com.edusoft.auth.dto.SignInDTO;
import com.edusoft.auth.mapper.DTOMapper;
import com.edusoft.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.edusoft.auth.dto.ResponseDataStatus.ERROR;
import static com.edusoft.auth.dto.ResponseDataStatus.SUCCESS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DTOMapper dtoMapper;

    @GetMapping("/welcome")
    public ResponseEntity<ResponseData> welcome() {
        return new ResponseEntity<>(ResponseData.builder()
                .status(SUCCESS.toString())
                .message("Welcome to my app")
                .data("Connect success").build(), OK);
    }

    @GetMapping("/change-password")
    public ResponseEntity<ResponseData> changePassword(@Validated @RequestBody final ChangePasswordDTO changePasswordDTO) {
        try {
            userService.changePassword(changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Change password successful").build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @PostMapping("/usernames")
    public ResponseEntity<?> getUserByUsernames(@RequestBody List<String> usernames) {
        try {
            return new ResponseEntity<>(dtoMapper.toUserAppDTO(userService.getUserByUsernames(usernames)), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @PostMapping("/username")
    public ResponseEntity<ResponseData> getUserByUsername(@RequestBody String username) {
        try {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Get user by username successful")
                    .data(dtoMapper.toUserAppDTO(userService.getUserByUsername(username))).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> getCurrentUser() {
        try {
            return new ResponseEntity<>(dtoMapper.toUserAppDTO(userService.getCurrentUser()), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ResponseData> signIn(@Validated @RequestBody SignInDTO signInDTO) {
        try {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.toString())
                    .message("Sign in successful")
                    .data(userService.signIn(signInDTO.getUsername(), signInDTO.getPassword())).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.toString())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

}
