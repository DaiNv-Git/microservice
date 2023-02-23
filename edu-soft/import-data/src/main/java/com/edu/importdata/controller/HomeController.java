package com.edu.importdata.controller;

import com.edu.importdata.dto.ResponseData;
import com.edu.importdata.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.edu.importdata.dto.ResponseDataStatus.ERROR;
import static com.edu.importdata.dto.ResponseDataStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HomeController {

    @Autowired
    private ImportService importService;

    @GetMapping("/welcome")
    public ResponseEntity<?> welcome() {
        return new ResponseEntity<>("This is import-data service", HttpStatus.OK);
    }

    @GetMapping("/init-all")
    public ResponseEntity<ResponseData> initialAll() {
        try {
            importService.initialAll();
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Initial all successful").build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/init-user")
    public ResponseEntity<ResponseData> initialUsers() {
        try {
            importService.initialUsers();
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Initial user successful").build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/init-room")
    public ResponseEntity<ResponseData> initialRooms() {
        try {
            importService.initialRooms();
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Initial rooms successful").build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/init-grade")
    public ResponseEntity<ResponseData> initialGrades() {
        try {
            importService.initialGrades();
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Initial grades successful").build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/init-subject")
    public ResponseEntity<ResponseData> initialSubjects() {
        try {
            importService.initialSubjects();
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Initial subjects successful").build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/init-subject-member")
    public ResponseEntity<ResponseData> initialSubjectMembers() {
        try {
            importService.initialSubjectMembers();
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Initial subject members successful").build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/init-exam")
    public ResponseEntity<ResponseData> initialExams() {
        try {
            importService.initialExams();
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Initial exams successful").build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }
}
