package com.edu.main.function.service.impl;

import com.edu.main.function.clients.AuthClients;
import com.edu.main.function.dto.SubjectDTO;
import com.edu.main.function.dto.TimeTableDTO;
import com.edu.main.function.dto.UserAppDTO;
import com.edu.main.function.entity.Subject;
import com.edu.main.function.entity.SubjectMember;
import com.edu.main.function.mapper.DTOMapper;
import com.edu.main.function.repository.SubjectRepository;
import com.edu.main.function.service.MailService;
import com.edu.main.function.service.NotificationService;
import com.edu.main.function.service.SubjectMemberService;
import com.edu.main.function.service.SubjectService;
import com.edu.main.function.task.NotifyBeforeSubjectTask;
import com.edu.main.function.utils.DateUtil;
import javassist.NotFoundException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {

    private static final ZoneId ZONE_HCM = TimeZone.getTimeZone("Asia/Ho_Chi_Minh").toZoneId();

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private DTOMapper dtoMapper;

    @Autowired
    private MailService mailService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AuthClients authClients;

    @Autowired
    private SubjectMemberService subjectMemberService;

    @Override
    public List<Subject> save(List<SubjectDTO> subjectDTOS) {
        List<Subject> existedSubjects = subjectRepository.findAll();
        for (SubjectDTO subjectDTO : subjectDTOS) {
            if (existedSubjects.stream().anyMatch(s -> subjectDTO.getCode().equals(s.getCode()))) {
                throw new IllegalArgumentException("Subject code already exist!");
            }
        }
        List<Subject> subjects = dtoMapper.toSubject(subjectDTOS);
        subjects = subjectRepository.saveAll(subjects);
        subjects.forEach(subject -> this.createScheduleForNotifySubject(subject));
        return subjects;
    }

    @Override
    public Subject getById(Long id) throws NotFoundException {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subject not found"));
    }

    @Override
    public List<Subject> getByIds(List<Long> ids) {
        return subjectRepository.findByIdIn(ids);
    }

    @Override
    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }

    @Override
    public void createScheduleForNotifySubject(Subject subject) {
        Date startDate = subject.getStartDate();
        LocalDateTime dateTime = LocalDateTime.ofInstant(startDate.toInstant(), ZONE_HCM);
        String time = subject.getStartTime();
        if (StringUtils.isBlank(time)) {
            throw new IllegalArgumentException("Start time can not be empty!");
        }
        String[] times = time.split(":");
        dateTime = dateTime.withHour(Integer.parseInt(times[0]));
        dateTime = dateTime.withMinute(Integer.parseInt(times[1]));
        dateTime = dateTime.minus(Duration.of(30, ChronoUnit.MINUTES));

//        //Mock to testing
//        dateTime = LocalDateTime.ofInstant(new Date().toInstant(), ZONE_HCM);
//        dateTime = dateTime.plus(Duration.of(2, ChronoUnit.MINUTES));
//        //
        Date startSchedule = Date.from(dateTime.atZone(ZONE_HCM).toInstant());
        Timer timer = new Timer();
        TimerTask task = new NotifyBeforeSubjectTask(subject, mailService, notificationService,
                subjectMemberService, authClients);
        timer.schedule(task, startSchedule);
    }

    @Override
    public List<Subject> getByCodes(List<String> codes) {
        return subjectRepository.findByCodeIn(codes);
    }

    @Override
    public Subject getByCode(String code) {
        return subjectRepository.findByCode(code);
    }

    @Override
    public List<Subject> getSubjectOfCurrentUser() {
        UserAppDTO currentUser = authClients.getCurrentUser();
        if (currentUser == null || StringUtils.isBlank(currentUser.getUsername())) {
            throw new IllegalArgumentException("Cant not get information of current user");
        }
        if (!CollectionUtils.isEmpty(currentUser.getRoles())
                && currentUser.getRoles().stream().anyMatch(r -> "ADMIN".equalsIgnoreCase(r))) {
            return subjectRepository.findAll();
        }
        List<SubjectMember> subjectMembers = subjectMemberService.getByUsername(currentUser.getUsername());
        if (!CollectionUtils.isEmpty(subjectMembers)) {
            List<Long> subjectIds = subjectMembers.stream().map(s -> s.getSubject().getId()).collect(Collectors.toList());
            return this.getByIds(subjectIds);
        }
        return new ArrayList<>();
    }

    @Override
    public List<TimeTableDTO> getTimeTableOfCurrentUser(Date fromDate, Date toDate) {
        UserAppDTO currentUser = authClients.getCurrentUser();
        if (currentUser == null || StringUtils.isBlank(currentUser.getUsername())) {
            throw new IllegalArgumentException("Cant not get information of current user");
        }
        if (!CollectionUtils.isEmpty(currentUser.getRoles())
                && currentUser.getRoles().stream().anyMatch(r -> "ADMIN".equalsIgnoreCase(r))) {
            throw new IllegalArgumentException("Cant not get time table of admin");
        }
        List<TimeTableDTO> timeTableDTOs = new ArrayList<>();
        List<SubjectMember> subjectMembers = subjectMemberService.getByUsername(currentUser.getUsername());
        if (!CollectionUtils.isEmpty(subjectMembers)) {
            List<Long> subjectIds = subjectMembers.stream().map(s -> s.getSubject().getId()).collect(Collectors.toList());
            List<Subject> subjects = this.getByIds(subjectIds);
            if (!CollectionUtils.isEmpty(subjects)) {
                List<Subject> subjectOfUser = subjectRepository.findByIdIn(subjectIds);
                if (fromDate != null || toDate != null) {
                    subjectOfUser = subjectRepository
                            .findByIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(subjectIds, fromDate, toDate);
                }
                timeTableDTOs = dtoMapper.toTimeTableDTO(subjectOfUser);
                if (fromDate != null && toDate != null) {
                    LocalDateTime fromDateTime = LocalDateTime.ofInstant(fromDate.toInstant(), ZoneId.systemDefault());
                    LocalDateTime toDateTime = LocalDateTime.ofInstant(toDate.toInstant(), ZoneId.systemDefault());
                    for (TimeTableDTO timeTableDTO : timeTableDTOs) {
                        LocalDateTime startDateTime = LocalDateTime.ofInstant(timeTableDTO.getStartDate().toInstant(), ZoneId.systemDefault());
                        startDateTime = startDateTime.withHour(0);
                        startDateTime = startDateTime.withMinute(0);
                        LocalDateTime enDateTime = LocalDateTime.ofInstant(timeTableDTO.getEndDate().toInstant(), ZoneId.systemDefault());
                        while (startDateTime.isBefore(enDateTime)) {
                            if ((startDateTime.isAfter(fromDateTime) || startDateTime.isEqual(fromDateTime))
                                    && (startDateTime.isBefore(toDateTime) || startDateTime.isEqual(toDateTime))) {
                                timeTableDTO.setStartDate(DateUtil.setTimeForDate(Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant()),
                                        timeTableDTO.getStartTime()));
                                timeTableDTO.setEndDate(DateUtil.setTimeForDate(Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant()),
                                        timeTableDTO.getEndTime()));
                                break;
                            }
                            startDateTime = startDateTime.plusDays(7L);
                        }
                    }
                } else {
                    // Get all
                    List<TimeTableDTO> newTimeTables = new ArrayList<>();
                    for (TimeTableDTO timeTableDTO : timeTableDTOs) {
                        LocalDateTime startDateTime = LocalDateTime.ofInstant(timeTableDTO.getStartDate().toInstant(), ZoneId.systemDefault());
                        startDateTime = startDateTime.withHour(0);
                        startDateTime = startDateTime.withMinute(0);
                        LocalDateTime enDateTime = LocalDateTime.ofInstant(timeTableDTO.getEndDate().toInstant(), ZoneId.systemDefault());
                        while (startDateTime.isBefore(enDateTime)) {
                            TimeTableDTO newTimeTable = TimeTableDTO.builder()
                                    .subjectName(timeTableDTO.getSubjectName())
                                    .roomName(timeTableDTO.getRoomName())
                                    .startTime(timeTableDTO.getStartTime())
                                    .endTime(timeTableDTO.getEndTime())
                                    .build();
                            newTimeTable.setStartDate(DateUtil.setTimeForDate(Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant()),
                                    timeTableDTO.getStartTime()));
                            newTimeTable.setEndDate(DateUtil.setTimeForDate(Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant()),
                                    timeTableDTO.getEndTime()));
                            newTimeTables.add(newTimeTable);
                            startDateTime = startDateTime.plusDays(7L);
                        }
                    }
                    if (!CollectionUtils.isEmpty(newTimeTables)) {
                        timeTableDTOs = newTimeTables;
                    }
                }
            }
        }
        return timeTableDTOs;
    }
}
