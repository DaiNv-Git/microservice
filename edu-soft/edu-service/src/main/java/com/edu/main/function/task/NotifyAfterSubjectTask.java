package com.edu.main.function.task;

import com.edu.main.function.clients.AuthClients;
import com.edu.main.function.dto.UserAppDTO;
import com.edu.main.function.dto.enums.ReferenceNotify;
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
public class NotifyAfterSubjectTask extends TimerTask {

    private static final ZoneId ZONE_HCM = TimeZone.getTimeZone("Asia/Ho_Chi_Minh").toZoneId();

    private Subject subject;
    private MailService mailService;
    private NotificationService notificationService;
    private SubjectMemberService subjectMemberService;
    private AuthClients authClients;

    @SneakyThrows
    @Override
    public void run() {
        if (subject != null) {
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
                String message = "You are later for your " + subject.getName() + " class at room "
                        + subject.getRoomName() + ". Hurry up !!!";
                for (UserAppDTO userAppDTO : userAppDTOs) {
                    // Schedule after 15'
                    // Create notification
                    notificationService.save(Notification.builder()
                            .title("Schedule for subject")
                            .message(message)
                            .reference(referenceNotify)
                            .isRead(Boolean.FALSE)
                            .isImportant(Boolean.FALSE)
                            .isSystem(Boolean.TRUE)
                            .isUrgent(Boolean.FALSE)
                            .build());
                    // Send mail
                    mailService.sendMailNotifyAfterSubject(userAppDTO.getEmail(), subject.getName(), subject.getRoomName());
                }
            }
        }
        // Schedule for next week
        LocalDateTime dateTime = DateUtil.getStartDateForToday(subject.getStartDate());
        dateTime = dateTime.plus(Duration.of(7, ChronoUnit.DAYS));
        dateTime = dateTime.minus(Duration.of(30, ChronoUnit.MINUTES));
        Date nextSchedule = Date.from(dateTime.atZone(ZONE_HCM).toInstant());
        Timer timer = new Timer();
        TimerTask task = new NotifyAfterSubjectTask(subject, mailService, notificationService,
                subjectMemberService, authClients);
        timer.schedule(task, nextSchedule);
    }
}
