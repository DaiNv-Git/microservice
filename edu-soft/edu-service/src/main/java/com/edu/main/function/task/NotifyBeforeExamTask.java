package com.edu.main.function.task;

import com.edu.main.function.clients.AuthClients;
import com.edu.main.function.dto.UserAppDTO;
import com.edu.main.function.dto.enums.ReferenceNotify;
import com.edu.main.function.entity.Exam;
import com.edu.main.function.entity.Notification;
import com.edu.main.function.entity.Subject;
import com.edu.main.function.entity.SubjectMember;
import com.edu.main.function.service.MailService;
import com.edu.main.function.service.NotificationService;
import com.edu.main.function.service.SubjectMemberService;
import com.edu.main.function.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotifyBeforeExamTask extends TimerTask {

    private static final ZoneId ZONE_HCM = TimeZone.getTimeZone("Asia/Ho_Chi_Minh").toZoneId();

    private Exam exam;
    private MailService mailService;
    private NotificationService notificationService;
    private SubjectMemberService subjectMemberService;
    private AuthClients authClients;

    @SneakyThrows
    @Override
    public void run() {
        LocalDateTime startTime = DateUtil.getStartDateForToday(exam.getTime());
        if (exam != null) {
            Subject subject = exam.getSubject();
            List<UserAppDTO> userAppDTOs = new ArrayList<>();
            List<SubjectMember> members = subjectMemberService.getMemberOfSubject(subject.getId());
            List<String> usernames = members.stream().map(SubjectMember::getUsername).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(usernames)) {
                userAppDTOs = authClients.getUserByUsernames(usernames);
            }
            if (!CollectionUtils.isEmpty(userAppDTOs)) {
                ReferenceNotify referenceNotify = ReferenceNotify.valueOf(subject.getCode());
                if (referenceNotify == null) {
                    throw new IllegalArgumentException("Can not get reference for notify");
                }
                String message = "Your " + subject.getName()
                        + " examination is going to start in 30 minutes at room " + exam.getRoomName() + ". Good luck !";
                for (UserAppDTO userAppDTO : userAppDTOs) {
                    // Schedule before 30'
                    // Create notification
                    notificationService.save(Notification.builder().message(message)
                            .title("Schedule for exam")
                            .reference(referenceNotify)
                            .isRead(Boolean.FALSE)
                            .isImportant(Boolean.TRUE)
                            .isSystem(Boolean.TRUE)
                            .isUrgent(Boolean.FALSE)
                            .build());
                    // Send mail
                    mailService.sendMailNotifyBeforeExam(userAppDTO.getEmail(), subject.getName(), exam.getRoomName());

                    System.out.println("----------------------------------------------");
                    System.out.println("This is the notify before 30' at " + new Date());
                }
            }
        }
        // Schedule after 15'
        startTime = startTime.plus(Duration.of(15, ChronoUnit.MINUTES));
        Date nextSchedule = Date.from(startTime.atZone(ZONE_HCM).toInstant());
        Timer timer = new Timer();
        TimerTask task = new NotifyAfterExamTask(exam, mailService, notificationService,
                subjectMemberService, authClients);
        timer.schedule(task, nextSchedule);
    }
}
