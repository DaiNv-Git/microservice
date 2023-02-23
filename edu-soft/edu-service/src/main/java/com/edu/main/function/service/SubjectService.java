package com.edu.main.function.service;

import com.edu.main.function.dto.SubjectDTO;
import com.edu.main.function.dto.TimeTableDTO;
import com.edu.main.function.entity.Subject;
import javassist.NotFoundException;

import java.util.Date;
import java.util.List;

public interface SubjectService {

    List<Subject> save(List<SubjectDTO> subjectDTOS);

    Subject getById(final Long id) throws NotFoundException;

    List<Subject> getByIds(List<Long> ids);

    List<Subject> getAll();

    void createScheduleForNotifySubject(Subject subject);

    List<Subject> getByCodes(List<String> codes);

    Subject getByCode(String code);

    List<TimeTableDTO> getTimeTableOfCurrentUser(Date fromDate, Date toDate);

    List<Subject> getSubjectOfCurrentUser();
}
