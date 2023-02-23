package com.edusoft.auth.service;

import com.edusoft.auth.dto.UserAppDTO;
import com.edusoft.auth.entity.UserApp;
import javassist.NotFoundException;

import java.util.List;
import java.util.Map;

public interface UserService {

    Map<String, Object> signIn(final String username, final String password);

    void importUser(List<UserAppDTO> userAppDTOS);

    UserApp getCurrentUser() throws NotFoundException;

    UserApp getUserByUsername(final String username);

    List<UserApp> getUserByUsernames(final List<String> usernames);

    void changePassword(String oldPassword, String newPassword) throws IllegalArgumentException, NotFoundException;
}
