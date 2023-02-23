package com.edu.main.function.controller;

import com.edu.main.function.dto.ResponseData;
import com.edu.main.function.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.edu.main.function.dto.enums.ResponseDataStatus.ERROR;
import static com.edu.main.function.dto.enums.ResponseDataStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/time-table")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TimeTableController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public ResponseEntity<?> getTableOfCurrentUser(@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date fromDate,
                                                   @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date toDate) {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Get time table of current user successful")
                    .data(subjectService.getTimeTableOfCurrentUser(fromDate, toDate)).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }
}
