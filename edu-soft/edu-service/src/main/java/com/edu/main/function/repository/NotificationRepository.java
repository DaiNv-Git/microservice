package com.edu.main.function.repository;

import com.edu.main.function.dto.enums.ReferenceNotify;
import com.edu.main.function.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByReferenceIn(List<ReferenceNotify> references);

    List<Notification> findByIsSystem(Boolean isSystem);
}
