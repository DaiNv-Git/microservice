package com.edu.main.function.controller;

import com.edu.main.function.dto.ExamDTO;
import com.edu.main.function.dto.ResponseData;
import com.edu.main.function.mapper.DTOMapper;
import com.edu.main.function.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.edu.main.function.dto.enums.ResponseDataStatus.ERROR;
import static com.edu.main.function.dto.enums.ResponseDataStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/exams")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ExamController {

    @Autowired
    private ExamService examService;

    @Autowired
    private DTOMapper dtoMapper;

    @GetMapping
    public ResponseEntity<ResponseData> getExamOfCurrentUser() {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Initial exams successful")
                    .data(dtoMapper.toExamDTO(examService.getExamOfCurrentUser())).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/init")
    public ResponseEntity<ResponseData> initExams(@RequestBody List<ExamDTO> examDTOS) {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Initial exams successful")
                    .data(dtoMapper.toExamDTO(examService.save(examDTOS))).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }
}
