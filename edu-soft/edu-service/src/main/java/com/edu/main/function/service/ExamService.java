package com.edu.main.function.service;

import com.edu.main.function.dto.ExamDTO;
import com.edu.main.function.entity.Exam;

import java.util.List;

public interface ExamService {

    List<Exam> save(List<ExamDTO> examDTOS);

    void createScheduleForNotifyExam(Exam exam);

    List<Exam> getExamOfCurrentUser();
}
