package com.edu.main.function.repository;

import com.edu.main.function.dto.enums.Semester;
import com.edu.main.function.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findBySemester(final Semester semester);
    @Query(value = "select * from grade where username = ?1 and semester=?2",nativeQuery = true)
    List<Grade> findByUsernameAndSemester(final String username, final String semester);
    @Transactional
    @Modifying
    @Query(value = "delete from grade where id in(?1)",nativeQuery = true)
    void deleteAllIn(List<Long> ids);
}
