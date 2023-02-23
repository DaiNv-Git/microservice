package com.edusoft.auth.repository;

import com.edusoft.auth.entity.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserApp, Long> {

    UserApp findByUsername(final String username);

    List<UserApp> findByUsernameIn(final List<String> usernames);
}
