package com.edu.main.function.repository;

import com.edu.main.function.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Subject findByCode(final String code);

    List<Subject> findByIdIn(List<Long> ids);

    List<Subject> findByCodeIn(List<String> codes);

    List<Subject> findByIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(List<Long> subjectIds, Date fromDate, Date toDate);
}
