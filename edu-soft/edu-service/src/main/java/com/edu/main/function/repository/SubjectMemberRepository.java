package com.edu.main.function.repository;

import com.edu.main.function.entity.Subject;
import com.edu.main.function.entity.SubjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectMemberRepository extends JpaRepository<SubjectMember, Long> {

    List<SubjectMember> findBySubject(final Subject subject);

    List<SubjectMember> findByUsername(final String username);
}
