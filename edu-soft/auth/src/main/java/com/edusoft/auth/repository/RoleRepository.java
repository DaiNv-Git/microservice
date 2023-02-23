package com.edusoft.auth.repository;

import com.edusoft.auth.entity.RoleApp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleApp, Long> {

    RoleApp findByName(final String name);
}
