package com.edu.importdata.service;

import com.edu.importdata.dto.ExamDTO;
import com.edu.importdata.dto.GradeDTO;
import com.edu.importdata.dto.RoomDTO;
import com.edu.importdata.dto.SubjectDTO;
import com.edu.importdata.dto.SubjectMemberDTO;
import com.edu.importdata.dto.UserAppDTO;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ReadDataService {

    List<UserAppDTO> getUsersFromExcelFile(final String path) throws IOException;

    List<RoomDTO> getRoomsFromExcelFile(final String path) throws IOException;

    List<GradeDTO> getGradeFromExcelFile(final String path) throws IOException;

    List<GradeDTO> getAllGrade() throws IOException;

    List<GradeDTO> getGradesImport(String path) throws IOException;

    List<SubjectDTO> getSubjectFromExcelFile(final String path) throws IOException, ParseException;

    List<SubjectMemberDTO> getSubjectMemberFromExcelFile(List<SubjectDTO> subjectDTOs) throws IOException;

    List<ExamDTO> getExamFromExcelFile(final String path) throws IOException, ParseException;
}
