package com.edu.main.function.service.impl;

import com.edu.main.function.clients.AuthClients;
import com.edu.main.function.dto.ExamDTO;
import com.edu.main.function.dto.UserAppDTO;
import com.edu.main.function.entity.Exam;
import com.edu.main.function.entity.Subject;
import com.edu.main.function.entity.SubjectMember;
import com.edu.main.function.repository.ExamRepository;
import com.edu.main.function.service.ExamService;
import com.edu.main.function.service.MailService;
import com.edu.main.function.service.NotificationService;
import com.edu.main.function.service.SubjectMemberService;
import com.edu.main.function.service.SubjectService;
import com.edu.main.function.task.NotifyBeforeExamTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExamServiceImpl implements ExamService {

    private static final ZoneId ZONE_HCM = TimeZone.getTimeZone("Asia/Ho_Chi_Minh").toZoneId();

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private SubjectService subjecService;

    @Autowired
    private MailService mailService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SubjectMemberService subjectMemberService;

    @Autowired
    private AuthClients authClients;

    @Autowired
    private SubjectService subjectService;

    @Override
    public List<Exam> save(List<ExamDTO> examDTOS) {
        List<Exam> exams = new ArrayList<>();
        List<String> subjectCodes = examDTOS.stream().map(ExamDTO::getSubjectCode).filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        List<Subject> subjects = subjecService.getByCodes(subjectCodes);
        for (ExamDTO examDTO : examDTOS) {
            if (examDTO == null || examDTO.getTime() == null || StringUtils.isBlank(examDTO.getSubjectCode())) {
                throw new IllegalArgumentException("Data of exam invalid!");
            }
            Optional<Subject> subjectOptional = subjects.stream().filter(s -> examDTO.getSubjectCode()
                    .equalsIgnoreCase(s.getCode())).findFirst();
            if (!subjectOptional.isPresent()) {
                throw new IllegalArgumentException("Subject not found");
            }
            exams.add(Exam.builder().subject(subjectOptional.get()).time(examDTO.getTime())
                    .roomName(examDTO.getRoomName()).build());
        }
        exams = examRepository.saveAll(exams);
        exams.forEach(exam -> this.createScheduleForNotifyExam(exam));
        return exams;
    }

    @Override
    public void createScheduleForNotifyExam(Exam exam) {
        Date startDate = exam.getTime();
        LocalDateTime dateTime = LocalDateTime.ofInstant(startDate.toInstant(), ZONE_HCM);
        dateTime = dateTime.minus(Duration.of(30, ChronoUnit.MINUTES));

//        // Mock to testing
//        dateTime = LocalDateTime.ofInstant(new Date().toInstant(), ZONE_HCM);
//        dateTime = dateTime.plus(Duration.of(2, ChronoUnit.MINUTES));
//        //

        Date startSchedule = Date.from(dateTime.atZone(ZONE_HCM).toInstant());
        Timer timer = new Timer();
        TimerTask task = new NotifyBeforeExamTask(exam, mailService, notificationService,
                subjectMemberService, authClients);
        timer.schedule(task, startSchedule);
    }

    @Override
    public List<Exam> getExamOfCurrentUser() {
        UserAppDTO currentUser = authClients.getCurrentUser();
        if (!CollectionUtils.isEmpty(currentUser.getRoles())
                && currentUser.getRoles().stream().anyMatch(r -> "ADMIN".equalsIgnoreCase(r))) {
            return examRepository.findAll();
        }
        List<SubjectMember> subjectMembers = subjectMemberService.getByUsername(currentUser.getUsername());
        if (!CollectionUtils.isEmpty(subjectMembers)) {
            List<Long> subjectIds = subjectMembers.stream().map(s -> s.getSubject().getId()).collect(Collectors.toList());
            List<Subject> subjects = subjectService.getByIds(subjectIds);
            if (!CollectionUtils.isEmpty(subjects)) {
                return examRepository.findBySubjectIn(subjects);
            }
        }
        return new ArrayList<>();
    }
}
