package com.edu.main.function.service.impl;

import com.edu.main.function.clients.AuthClients;
import com.edu.main.function.dto.GradeDTO;
import com.edu.main.function.dto.UserAppDTO;
import com.edu.main.function.dto.enums.Semester;
import com.edu.main.function.entity.Grade;
import com.edu.main.function.mapper.DTOMapper;
import com.edu.main.function.repository.GradeRepository;
import com.edu.main.function.service.GradeService;
import javassist.NotFoundException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private DTOMapper dtoMapper;

    @Autowired
    private AuthClients authClients;

    @Override
    public List<Grade> save(List<GradeDTO> gradeDTOS) throws NotFoundException {
        if (CollectionUtils.isEmpty(gradeDTOS)) {
            throw new IllegalStateException("List grade can not be null");
        }
        List<Grade> grades = dtoMapper.toGrade(gradeDTOS);
        return gradeRepository.saveAll(grades);
    }

    @Override
    public List<Grade> getGradesOfUser(String username, String sem) {
        Semester semester = dtoMapper.convertToSemester(sem);
        if (StringUtils.isBlank(username)) {
            UserAppDTO currentUser = authClients.getCurrentUser();
            if (currentUser == null) {
                throw new IllegalStateException("Can not get info of current user");
            }
            username = currentUser.getUsername();
        }
        if (username.equalsIgnoreCase("ADMIN")) {
            return gradeRepository.findBySemester(semester);
        }
        return gradeRepository.findByUsernameAndSemester(username, semester.toString());
    }

    @Override
    public void deleteAllByIds(List<Long> ids) {
        gradeRepository.deleteAllIn(ids);
    }

    @Override
    public List<Grade> importExcel(MultipartFile file) throws IOException {
        List<GradeDTO> data = getDataImport(file);
        List<Grade> grades = dtoMapper.toGrade(data);
        return gradeRepository.saveAll(grades);
    }

    @Override
    public Grade detail(Long id) {
        return gradeRepository.findById(id).get();
    }

    public List<GradeDTO> getDataImport(MultipartFile file) throws IOException {
        List<GradeDTO> gradeDTOS = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String userName = row.getCell(0).getStringCellValue();
            Double cgpa100 = row.getCell(1) != null ? Double.parseDouble(row.getCell(1).getStringCellValue()) : 0.0;
            String creditsAccumulated = row.getCell(2).getStringCellValue();
            String classification = row.getCell(3).getStringCellValue();
            String semester = row.getCell(4).getStringCellValue();
            Double gpa4 =row.getCell(5) != null ? Double.parseDouble(row.getCell(5).getStringCellValue()) : 0.0;
            GradeDTO gradeDTO = new GradeDTO(userName, cgpa100, creditsAccumulated,
                    classification, semester,gpa4);
            gradeDTOS.add(gradeDTO);
        }
        return gradeDTOS;
    }
}
