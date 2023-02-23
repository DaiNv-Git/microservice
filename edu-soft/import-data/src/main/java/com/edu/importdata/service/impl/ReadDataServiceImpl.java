package com.edu.importdata.service.impl;

import com.edu.importdata.constants.Constant;
import com.edu.importdata.dto.ExamDTO;
import com.edu.importdata.dto.GradeDTO;
import com.edu.importdata.dto.RoomDTO;
import com.edu.importdata.dto.SubjectDTO;
import com.edu.importdata.dto.SubjectMemberDTO;
import com.edu.importdata.dto.UserAppDTO;
import com.edu.importdata.service.ReadDataService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;


@Service
public class ReadDataServiceImpl implements ReadDataService {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static TimeZone TIMEZONE_HCM = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");

    @Override
    public List<GradeDTO> getAllGrade() throws IOException {
        List<GradeDTO> gradeDTOS = this.getGradeFromExcelFile(Constant.PATH_FILE_GRADE_SEM_2_2);
        gradeDTOS.addAll(this.getGradeFromExcelFile(Constant.PATH_FILE_GRADE_SEM_1_3));
        gradeDTOS.addAll(this.getGradeFromExcelFile(Constant.PATH_FILE_GRADE_SEM_2_3));
        gradeDTOS.addAll(this.getGradeFromExcelFile(Constant.PATH_FILE_GRADE_SEM_1_4));
        return gradeDTOS;
    }

    @Override
    public List<GradeDTO> getGradesImport(String path) throws IOException {
        List<GradeDTO> gradeDTOS = new ArrayList<>();
        FileInputStream inputStream = new FileInputStream(path);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 1 ; i <= sheet.getLastRowNum();i++) {
            Row row = sheet.getRow(i);
            String userName = row.getCell(0).getStringCellValue();
            Double cgpa100 = row.getCell(1) !=null ?row.getCell(1).getNumericCellValue():0.0;
            String creditsAccumulated = row.getCell(2).getStringCellValue();
            String classification = row.getCell(3).getStringCellValue();
            Double algorithms = row.getCell(4) !=null ? row.getCell(4).getNumericCellValue():0.0;
            Double probabilityStatistics=row.getCell(5) !=null ? row.getCell(5).getNumericCellValue():0.0;
            Double advancedMath = row.getCell(6) !=null ? row.getCell(6).getNumericCellValue():0.0;
            String semester = row.getCell(7).getStringCellValue();
            GradeDTO gradeDTO = new GradeDTO(userName,cgpa100,creditsAccumulated,
                    classification,algorithms,probabilityStatistics,advancedMath,semester);
            gradeDTOS.add(gradeDTO);
        }
        return gradeDTOS;
    }

    @Override
    public List<GradeDTO> getGradeFromExcelFile(String path) throws IOException {
        List<GradeDTO> gradeDTOS = new ArrayList<>();
        String semester = "SEM_2_2";
        switch (path) {
            case Constant.PATH_FILE_GRADE_SEM_1_3:
                semester = "SEM_1_3";
                break;
            case Constant.PATH_FILE_GRADE_SEM_2_3:
                semester = "SEM_2_3";
                break;
            case Constant.PATH_FILE_GRADE_SEM_1_4:
                semester = "SEM_1_4";
                break;
            default:
                break;
        }
        File file = this.getFile(path);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(inputStream);
        if (file == null) {
            throw new IOException("File grade not found");
        }
        Sheet firstSheet = workbook.getSheetAt(0);
        for (int i = 1; i <= firstSheet.getLastRowNum(); i++) {
            Row row = firstSheet.getRow(i);
            final String username = getStringFromCell(row.getCell(0));
            final String cgpa100 = getStringFromCell(row.getCell(2));
            final String creditsAccumulated = getStringFromCell(row.getCell(3));
            final String classification = getStringFromCell(row.getCell(4));
            gradeDTOS.add(GradeDTO.builder().username(username)
                    .cgpa100(StringUtils.isBlank(cgpa100) ? 0.0 : Double.parseDouble(cgpa100))
                    .creditsAccumulated(creditsAccumulated)
                    .classification(classification)
                    .semester(semester).build());
        }
        workbook.close();
        inputStream.close();
        return gradeDTOS;
    }

    @Override
    public List<RoomDTO> getRoomsFromExcelFile(String path) throws IOException {
        List<RoomDTO> roomDTOS = new ArrayList<>();
        File file = this.getFile(path);
        if (file == null) {
            throw new IOException("File rooms not found");
        }
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        for (int i = 1; i <= firstSheet.getLastRowNum(); i++) {
            Row row = firstSheet.getRow(i);
            roomDTOS.add(RoomDTO.builder().name(getStringFromCell(row.getCell(0))).build());
        }
        workbook.close();
        inputStream.close();
        return roomDTOS;
    }

    @Override
    public List<UserAppDTO> getUsersFromExcelFile(final String path) throws IOException {
        List<UserAppDTO> userAppDTOS = new ArrayList<>();
        File file = this.getFile(path);
        if (file == null) {
            throw new IOException("File users not found");
        }
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        for (int i = 1; i <= firstSheet.getLastRowNum(); i++) {
            Row row = firstSheet.getRow(i);
            String username = getStringFromCell(row.getCell(0));
            String password = getStringFromCell(row.getCell(1));
            String strRole = getStringFromCell(row.getCell(2));
            String email = getStringFromCell(row.getCell(3));
            String[] roles = strRole.split(",");
            UserAppDTO userAppDTO = UserAppDTO.builder().username(username).password(password)
                    .roles(Arrays.asList(roles)).email(email).build();
            userAppDTOS.add(userAppDTO);
        }
        workbook.close();
        inputStream.close();
        return userAppDTOS;
    }

    @Override
    public List<SubjectDTO> getSubjectFromExcelFile(String path) throws IOException, ParseException {
        List<SubjectDTO> subjectDTOS = new ArrayList<>();
        File file = this.getFile(path);
        if (file == null) {
            throw new IOException("File courses not found");
        }
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
//        startDateFormat.setTimeZone(TIMEZONE_HCM);
//        endDateFormat.setTimeZone(TIMEZONE_HCM);
        for (int i = 1; i <= firstSheet.getLastRowNum(); i++) {
            Row row = firstSheet.getRow(i);
            if (row != null) {
                String code = getStringFromCell(row.getCell(0));
                String name = getStringFromCell(row.getCell(1));
                String startTime = getStringFromCell(row.getCell(2));
                String endTime = getStringFromCell(row.getCell(3));
                String startDate = getStringFromCell(row.getCell(4));
                String endDate = getStringFromCell(row.getCell(5));
                String roomName = getStringFromCell(row.getCell(6));
                subjectDTOS.add(SubjectDTO.builder().code(code).name(name).roomName(roomName).startDate(dateFormat.parse(startDate))
                        .endDate(dateFormat.parse(endDate)).startTime(startTime).endTime(endTime).build());
            }
        }
        workbook.close();
        inputStream.close();
        return subjectDTOS;
    }

    @Override
    public List<SubjectMemberDTO> getSubjectMemberFromExcelFile(List<SubjectDTO> subjectDTOs)
            throws IOException {
        List<SubjectMemberDTO> memberDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(subjectDTOs)) {
            for (SubjectDTO subjectDTO : subjectDTOs) {
                final String subjectCode = subjectDTO.getCode();
                File file = this.getFile(Constant.PATH_LOCATION_SUBJECT_MEMBER + subjectCode + ".xlsx");
                if (file != null) {
                    FileInputStream inputStream = new FileInputStream(file);
                    Workbook workbook = new XSSFWorkbook(inputStream);
                    Sheet firstSheet = workbook.getSheetAt(0);
                    for (int i = 1; i <= firstSheet.getLastRowNum(); i++) {
                        Row row = firstSheet.getRow(i);
                        String username = getStringFromCell(row.getCell(0));
                        memberDTOS.add(SubjectMemberDTO.builder().username(username)
                                .subjectId(subjectDTO.getId()).build());
                    }
                    workbook.close();
                    inputStream.close();
                }
            }
        }
        return memberDTOS;
    }

    @Override
    public List<ExamDTO> getExamFromExcelFile(String path) throws IOException, ParseException {
        List<ExamDTO> examDTOs = new ArrayList<>();
        File file = this.getFile(path);
        if (file == null) {
            throw new IOException("File exam not found");
        }
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        dateTimeFormat.setTimeZone(TIMEZONE_HCM);
        for (int i = 1; i <= firstSheet.getLastRowNum(); i++) {
            Row row = firstSheet.getRow(i);
            if (row != null) {
                String subjectCode = getStringFromCell(row.getCell(0));
//                String name = getStringFromCell(row.getCell(1));
                String time = getStringFromCell(row.getCell(2));
                String date = getStringFromCell(row.getCell(3));
                String roomName = getStringFromCell(row.getCell(4));
                if (!StringUtils.isBlank(date) && !StringUtils.isBlank(time)) {
                    date = date.trim() + " " + time.trim();
                }
                examDTOs.add(ExamDTO.builder().subjectCode(subjectCode).roomName(roomName)
                        .time(dateTimeFormat.parse(date)).build());
            }
        }
        workbook.close();
        inputStream.close();
        return examDTOs;
    }

    private String getStringFromCell(Cell cell) {
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }

    private File getFile(final String path) throws FileNotFoundException {
        return ResourceUtils.getFile("classpath:" + path);
    }
}
