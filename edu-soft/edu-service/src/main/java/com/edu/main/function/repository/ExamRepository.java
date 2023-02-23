package com.edu.main.function.repository;

import com.edu.main.function.entity.Exam;
import com.edu.main.function.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findBySubjectIn(List<Subject> subjects);
}
