package com.edu.main.function.task;

import com.edu.main.function.entity.Notification;
import com.edu.main.function.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ChangeStatusUrgentOfNotificationTask extends TimerTask {

    private Notification notification;
    private NotificationService notificationService;

    @Override
    public void run() {
        if (notification != null) {
            notification.setIsUrgent(Boolean.FALSE);
            notificationService.save(notification);
            log.info("Removed status urgent of notification: " + notification.getTitle());
        }
    }
}
