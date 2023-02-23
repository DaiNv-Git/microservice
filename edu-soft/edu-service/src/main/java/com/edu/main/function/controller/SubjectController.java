package com.edu.main.function.controller;

import com.edu.main.function.dto.ResponseData;
import com.edu.main.function.dto.SubjectDTO;
import com.edu.main.function.dto.SubjectMemberDTO;
import com.edu.main.function.entity.Subject;
import com.edu.main.function.mapper.DTOMapper;
import com.edu.main.function.service.SubjectMemberService;
import com.edu.main.function.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import static com.edu.main.function.dto.enums.ResponseDataStatus.ERROR;
import static com.edu.main.function.dto.enums.ResponseDataStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/subjects")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private DTOMapper dtoMapper;

    @Autowired
    private SubjectMemberService memberService;

    @GetMapping("/task")
    public void testSchedule() {
        Subject subject = new Subject();
        subject.setId(5L);
        subject.setCode("IT069IU");
        subject.setName("Test notify");
        subject.setStartDate(new Date());
        subjectService.createScheduleForNotifySubject(subject);
    }

    @GetMapping
    public ResponseEntity<?> getSubjectOfCurrentUser() {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Get subjects of current user successful")
                    .data(dtoMapper.toSubjectDTO(subjectService.getSubjectOfCurrentUser())).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestParam(required = false) boolean isFeign) {
        try {
            if (isFeign) {
                return new ResponseEntity<>(dtoMapper.toSubjectDTO(subjectService.getAll()), HttpStatus.OK);
            }
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Get all subjects successful")
                    .data(dtoMapper.toSubjectDTO(subjectService.getAll())).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/init")
    public ResponseEntity<ResponseData> initialSubjects(@RequestBody List<SubjectDTO> subjectDTOS) {
        try {
            subjectService.save(subjectDTOS);
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Initial subjects successful").build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/init-member")
    public ResponseEntity<ResponseData> initialSubjectMembers(
            @RequestBody List<SubjectMemberDTO> memberDTOS) {
        try {
            memberService.save(memberDTOS);
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Initial subject members successful").build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }
}
