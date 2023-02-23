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
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Collectors;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotifyAfterExamTask extends TimerTask {

    private Exam exam;
    private MailService mailService;
    private NotificationService notificationService;
    private SubjectMemberService subjectMemberService;
    private AuthClients authClients;

    @SneakyThrows
    @Override
    public void run() {
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
                String message = "You are late for your " + subject.getName() + " examination at room "
                        + exam.getRoomName() + ". Hurry up !!!";
                for (UserAppDTO userAppDTO : userAppDTOs) {
                    // Schedule after 15'
                    // Create notification
                    notificationService.save(Notification.builder()
                            .title("Schedule for exam")
                            .message(message)
                            .reference(referenceNotify)
                            .isRead(Boolean.FALSE)
                            .isImportant(Boolean.TRUE)
                            .isSystem(Boolean.TRUE)
                            .isUrgent(Boolean.FALSE)
                            .build());
                    // Send mail
                    mailService.sendMailNotifyAfterExam(userAppDTO.getEmail(), subject.getName(), exam.getRoomName());
                }
            }
        }
    }
}
