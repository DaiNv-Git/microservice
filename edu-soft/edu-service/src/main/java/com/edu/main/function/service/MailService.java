package com.edu.main.function.service;

import java.util.Date;

public interface MailService {

    void sendMailNotifyBeforeSubject(String to, String subjectName, Date time, String roomName);

    void sendMailNotifyAfterSubject(String to, String subjectName, String roomName);

    void sendMailNotifyBeforeExam(String to, String subjectName, String roomName);

    void sendMailNotifyAfterExam(String to, String subjectName, String roomName);
}
