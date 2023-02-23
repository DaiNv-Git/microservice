package com.edu.main.function.service;

import com.edu.main.function.dto.GradeDTO;
import com.edu.main.function.entity.Grade;
import javassist.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface GradeService {

    List<Grade> save(List<GradeDTO> gradeDTOS) throws NotFoundException;

    List<Grade> getGradesOfUser(String username, String sem);

    void deleteAllByIds(List<Long> ids);

    List<Grade> importExcel(MultipartFile file) throws IOException;

    Grade detail(Long id);
}
