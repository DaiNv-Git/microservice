package com.edu.main.function.service.impl;

import com.edu.main.function.dto.MailRequest;
import com.edu.main.function.dto.MailResponse;
import com.edu.main.function.service.MailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String fromMail;

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private Configuration configuration;

    public MailResponse sendMail(MailRequest request, String templateName, Map<String, Object> mapData) {
        MailResponse mailResponse = new MailResponse();
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            Template template = configuration.getTemplate(templateName);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, mapData);

            messageHelper.setTo(request.getTo());
            messageHelper.setText(html, true);
            messageHelper.setSubject(request.getSubject());
            messageHelper.setFrom(request.getFrom());
            sender.send(messageHelper.getMimeMessage());

            log.info("Mail sending to: " + request.getTo());
            mailResponse.setMessage("Mail sending to: " + request.getTo());
            mailResponse.setStatus(Boolean.TRUE);
        } catch (Exception e) {
            log.info("Mail sending failed: " + e.getMessage());
            mailResponse.setMessage("Mail sending failed: " + e.getMessage());
            mailResponse.setStatus(Boolean.FALSE);
        }
        return mailResponse;
    }

    @Override
    public void sendMailNotifyAfterSubject(String to, String subjectName, String roomName) {
        MailRequest mailRequest = new MailRequest(to, fromMail, "Notify Subject");
        Map<String, Object> mapData = new HashMap<>();
        mapData.put("subjectName", subjectName);
        mapData.put("roomName", roomName);
        sendMail(mailRequest, "mail-template-notify-after-subject.ftl", mapData);
    }

    @Override
    public void sendMailNotifyBeforeSubject(String to, String subjectName, Date time, String roomName) {
        MailRequest mailRequest = new MailRequest(to, fromMail, "Notify Subject");
        Map<String, Object> mapData = new HashMap<>();
        mapData.put("subjectName", subjectName);
        mapData.put("time", time.toString());
        mapData.put("roomName", roomName);
        sendMail(mailRequest, "mail-template-notify-before-subject.ftl", mapData);
    }

    @Override
    public void sendMailNotifyBeforeExam(String to, String subjectName, String roomName) {
        MailRequest mailRequest = new MailRequest(to, fromMail, "Notify Exam");
        Map<String, Object> mapData = new HashMap<>();
        mapData.put("subjectName", subjectName);
        mapData.put("roomName", roomName);
        sendMail(mailRequest, "mail-template-notify-before-exam.ftl", mapData);
    }

    @Override
    public void sendMailNotifyAfterExam(String to, String subjectName, String roomName) {
        MailRequest mailRequest = new MailRequest(to, fromMail, "Notify Exam");
        Map<String, Object> mapData = new HashMap<>();
        mapData.put("subjectName", subjectName);
        mapData.put("roomName", roomName);
        sendMail(mailRequest, "mail-template-notify-after-exam.ftl", mapData);
    }
}
