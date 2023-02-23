package com.edu.importdata.clients;
import com.edu.importdata.dto.ExamDTO;
import com.edu.importdata.dto.GradeDTO;
import com.edu.importdata.dto.RoomDTO;
import com.edu.importdata.dto.SubjectDTO;
import com.edu.importdata.dto.SubjectMemberDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "EDU-SERVICE")
public interface EduClients {

    @PostMapping("/api/v1/grades/init")
    void initGrades(@RequestBody List<GradeDTO> gradeDTOS);

    @PostMapping("/api/v1/rooms/init")
    void initRooms(@RequestBody List<RoomDTO> roomDTOs);

    @PostMapping("/api/v1/subjects/init")
    void initSubjects(@RequestBody List<SubjectDTO> subjectDTOS);

    @GetMapping("/api/v1/subjects/all")
    List<SubjectDTO> getAllSubject(@RequestParam(required = false, name = "isFeign") Boolean isFeign);

    @PostMapping("/api/v1/subjects/init-member")
    void initSubjectMembers(@RequestBody List<SubjectMemberDTO> memberDTOS);

    @PostMapping("/api/v1/exams/init")
    void initExams(@RequestBody List<ExamDTO> examDTOs);
}
