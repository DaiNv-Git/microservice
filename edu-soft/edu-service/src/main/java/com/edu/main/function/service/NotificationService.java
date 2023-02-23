package com.edu.main.function.service;

import com.edu.main.function.dto.NotificationDTO;
import com.edu.main.function.entity.Notification;
import javassist.NotFoundException;

import java.util.List;

public interface NotificationService {

    Notification save(Notification notification);

    List<Notification> getNotificationForCurrentUser();

    Notification createNotification(NotificationDTO notificationDTO) throws NotFoundException;

    Notification changeFlagIsRead(final Long id, final Boolean isRead) throws NotFoundException;

    Notification getById(final Long id) throws NotFoundException;
}
