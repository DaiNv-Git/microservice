package com.edu.main.function.service.impl;

import com.edu.main.function.clients.AuthClients;
import com.edu.main.function.dto.NotificationDTO;
import com.edu.main.function.dto.UserAppDTO;
import com.edu.main.function.dto.enums.ReferenceNotify;
import com.edu.main.function.entity.Notification;
import com.edu.main.function.entity.Subject;
import com.edu.main.function.entity.SubjectMember;
import com.edu.main.function.mapper.DTOMapper;
import com.edu.main.function.repository.NotificationRepository;
import com.edu.main.function.service.NotificationService;
import com.edu.main.function.service.SubjectMemberService;
import com.edu.main.function.service.SubjectService;
import com.edu.main.function.task.ChangeStatusUrgentOfNotificationTask;
import javassist.NotFoundException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

@Service
public class  NotificationServiceImpl implements NotificationService {

    private static final ZoneId ZONE_HCM = TimeZone.getTimeZone("Asia/Ho_Chi_Minh").toZoneId();

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private DTOMapper dtoMapper;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private AuthClients authClients;

    @Autowired
    private SubjectMemberService subjectMemberService;

    @Override
    public Notification save(Notification notification) {
        notification.setCreatedDate(new Date());
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getNotificationForCurrentUser() {
        UserAppDTO currentUser = authClients.getCurrentUser();
        if (currentUser == null || StringUtils.isBlank(currentUser.getUsername())) {
            throw new IllegalArgumentException("Cant not get information of current user");
        }
        if (!CollectionUtils.isEmpty(currentUser.getRoles())
                && currentUser.getRoles().stream().anyMatch(r -> "ADMIN".equalsIgnoreCase(r))) {
            return sortNotification(notificationRepository.findByIsSystem(Boolean.FALSE).stream()
                    .sorted(Comparator.comparingLong(Notification::getId).reversed()).collect(Collectors.toList()));
        }
        List<ReferenceNotify> references = new ArrayList<>();
        references.add(ReferenceNotify.ALL);
        List<SubjectMember> subjectMembers = subjectMemberService.getByUsername(currentUser.getUsername());
        if (!CollectionUtils.isEmpty(subjectMembers)) {
            List<Long> subjectIds = subjectMembers.stream().map(s -> s.getSubject().getId()).collect(Collectors.toList());
            List<Subject> subjects = subjectService.getByIds(subjectIds);
            if (!CollectionUtils.isEmpty(subjects)) {
                subjects.stream().forEach(s -> references.add(dtoMapper.convertToReferenceNotify(s.getCode())));
            }
        }
        return sortNotification(notificationRepository.findByReferenceIn(references).stream()
                .sorted(Comparator.comparingLong(Notification::getId).reversed()).collect(Collectors.toList()));
    }

    @Override
    public Notification createNotification(NotificationDTO notificationDTO) throws NotFoundException {
        Notification notification = dtoMapper.toNotification(notificationDTO);
        notification.setReference(ReferenceNotify.ALL);
        if (notificationDTO.getSubjectId() != null) {
            Subject subject = subjectService.getById(notificationDTO.getSubjectId());
            notification.setReference(dtoMapper.convertToReferenceNotify(subject.getCode()));
        }
        notification.setIsRead(Boolean.FALSE);
        notification.setIsImportant(Boolean.FALSE);
        notification.setIsSystem(Boolean.FALSE);
        notification.setCreatedDate(new Date());
        notification = notificationRepository.save(notification);
        LocalDateTime dateTime = LocalDateTime.now();
        dateTime = dateTime.plusDays(1);
        Timer timer = new Timer();
        TimerTask task = new ChangeStatusUrgentOfNotificationTask(notification, this);
        timer.schedule(task, Date.from(dateTime.atZone(ZONE_HCM).toInstant()));
        return notification;
    }

    @Override
    public Notification changeFlagIsRead(Long id, Boolean isRead) throws NotFoundException {
        Notification notification = this.getById(id);
        notification.setIsRead(isRead == null ? Boolean.FALSE : isRead);
        return notificationRepository.save(notification);
    }

    @Override
    public Notification getById(final Long id) throws NotFoundException {
        return notificationRepository.findById(id).orElseThrow(() -> new NotFoundException("Notification not found"));
    }

    private List<Notification> sortNotification(List<Notification> notifications) {
        Collections.sort(notifications, (n1, n2) -> {
            int urgentResult = Boolean.compare(n2.getIsUrgent(), n1.getIsUrgent());
            if (urgentResult != 0) {
                return urgentResult;
            }
            if (n1.getEventDate() != null && n2.getEventDate() != null) {
                Date now = new Date();
                if (n1.getEventDate().before(now)) {
                    return 1;
                }
                if (n2.getEventDate().before(now)) {
                    return -1;
                }
                return n1.getEventDate().compareTo(n2.getEventDate());
            }
            return 1;
        });
        return notifications;
    }
}
