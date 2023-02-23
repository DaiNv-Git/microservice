package com.edu.importdata.service.impl;

import com.edu.importdata.clients.AuthClients;
import com.edu.importdata.clients.EduClients;
import com.edu.importdata.constants.Constant;
import com.edu.importdata.dto.ExamDTO;
import com.edu.importdata.dto.GradeDTO;
import com.edu.importdata.dto.RoomDTO;
import com.edu.importdata.dto.SubjectDTO;
import com.edu.importdata.dto.SubjectMemberDTO;
import com.edu.importdata.dto.UserAppDTO;
import com.edu.importdata.service.ImportService;
import com.edu.importdata.service.ReadDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ImportServiceImpl implements ImportService {

    @Autowired
    private ReadDataService readDataService;

    @Autowired
    private AuthClients authClients;

    @Autowired
    private EduClients eduClients;

    @Override
    public void initialAll() throws IOException, ParseException {
        this.initialUsers();
        this.initialRooms();
        this.initialGrades();
        this.initialSubjects();
        this.initialSubjectMembers();
        this.initialExams();
    }

    @Override
    public void initialUsers() throws IOException {
        List<UserAppDTO> userAppDTOS = readDataService.getUsersFromExcelFile(Constant.PATH_FILE_USER);
        if (!CollectionUtils.isEmpty(userAppDTOS)) {
            authClients.importUser(userAppDTOS);
            log.info("Initial user successful...");
        }
    }

    @Override
    public void initialRooms() throws IOException {
        List<RoomDTO> roomDTOS = readDataService.getRoomsFromExcelFile(Constant.PATH_FILE_ROOM);
        if (!CollectionUtils.isEmpty(roomDTOS)) {
            eduClients.initRooms(roomDTOS);
            log.info("Initial rooms successful...");
        }
    }

    @Override
    public void initialGrades() throws IOException {
        List<GradeDTO> gradeDTOS = readDataService.getAllGrade();
        if (!CollectionUtils.isEmpty(gradeDTOS)) {
            eduClients.initGrades(gradeDTOS);
            log.info("Initial grades successful...");
        }
    }


    @Override
    public void importGrades(String path) throws IOException {
        List<GradeDTO> gradeDTOS  =readDataService.getGradesImport("C:\\Users\\Dai\\Downloads\\Thesis_EventChecker\\Thesis_EventChecker\\EduSoft\\"+path);
        eduClients.initGrades(gradeDTOS);
    }



    @Override
    public void initialSubjects() throws IOException, ParseException {
        List<SubjectDTO> subjectDTOS = readDataService.getSubjectFromExcelFile(Constant.PATH_FILE_COURSES);
        if (!CollectionUtils.isEmpty(subjectDTOS)) {
            eduClients.initSubjects(subjectDTOS);
            log.info("Initial subjects successful...");
        }
    }

    @Override
    public void initialSubjectMembers() throws IOException {
        List<SubjectDTO> existedSubjects = eduClients.getAllSubject(Boolean.TRUE);
        if (!CollectionUtils.isEmpty(existedSubjects)) {
            List<SubjectMemberDTO> memberDTOs =
                    readDataService.getSubjectMemberFromExcelFile(existedSubjects);
            if (!CollectionUtils.isEmpty(memberDTOs)) {
                eduClients.initSubjectMembers(memberDTOs);
                log.info("Initial subject members successful...");
            }
        }
    }

    @Override
    public void initialExams() throws IOException, ParseException {
        List<ExamDTO> examDTOs = readDataService.getExamFromExcelFile(Constant.PATH_FILE_EXAM);
        if (!CollectionUtils.isEmpty(examDTOs)) {
            eduClients.initExams(examDTOs);
            log.info("Initial exams successful...");
        }
    }
}
