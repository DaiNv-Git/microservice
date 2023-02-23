package com.edu.main.function.controller;

import com.edu.main.function.dto.NotificationDTO;
import com.edu.main.function.dto.ResponseData;
import com.edu.main.function.mapper.DTOMapper;
import com.edu.main.function.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.edu.main.function.dto.enums.ResponseDataStatus.ERROR;
import static com.edu.main.function.dto.enums.ResponseDataStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/notifications")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private DTOMapper dtoMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getNotificationById(@PathVariable final Long id) {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Get notifications by id successful")
                    .data(dtoMapper.toNotificationDTO(notificationService.getById(id)))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<ResponseData> getNotificationForCurrentUser() {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Get notifications for current user successful")
                    .data(dtoMapper.toNotificationDTO(notificationService.getNotificationForCurrentUser()))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<ResponseData> createNotification(@Validated @RequestBody NotificationDTO notificationDTO) {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Get notifications successful")
                    .data(dtoMapper.toNotificationDTO(notificationService.createNotification(notificationDTO)))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData> changeFlagIsReadOfNotification(@PathVariable final Long id,
                                                                       @RequestParam Boolean isRead) {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Change flag isRead successful")
                    .data(dtoMapper.toNotificationDTO(notificationService.changeFlagIsRead(id, isRead)))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }
}
