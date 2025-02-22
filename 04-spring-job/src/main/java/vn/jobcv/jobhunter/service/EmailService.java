package vn.jobcv.jobhunter.service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
@Slf4j
@Service
public class EmailService {

    private final MailSender mailSender;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("spring.mail.username")
    private String emailFrom;


    public EmailService(MailSender mailSender,
            JavaMailSender javaMailSender,
            SpringTemplateEngine templateEngine, KafkaTemplate<String, String> kafkaTemplate) {
        this.mailSender = mailSender;
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendSimpleEmail() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("ads.hoidanit@gmail.com");
        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World from Spring Boot Email");
        this.mailSender.send(msg);
    }

    public void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        // Prepare message using a Spring helper
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content, isHtml);
            this.javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            System.out.println("ERROR SEND EMAIL: " + e);
        }
    }
    @KafkaListener(topics = "confirm-account-topic", groupId = "confirm-account-group")
    public void sendEmailByKafka(String messageReceive) throws MessagingException, UnsupportedEncodingException {
        log.info("Sending link to user, email={}", messageReceive);

        String[] arr = messageReceive.split(",", 3);
        String emailTo = arr[0].substring(arr[0].indexOf('=') + 1);
        String subject = arr[1].substring(arr[1].indexOf('=') + 1);
        String content = arr[2].substring(arr[2].indexOf('=') + 1);
        log.info("Generated email content: {}", content);


        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
            message.setFrom(emailFrom,"Job CV");
            message.setTo(emailTo);
            message.setSubject(subject);
            message.setText(content, true);
            this.javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            System.out.println("ERROR SEND EMAIL: " + e);
        }
        log.info("Email has sent to user, email={}, linkConfirm={}", emailTo);
    }

@Async
    public void sendEmailFromTemplateSync(
            String to,
            String subject,
            String templateName,
            String username,
            Object value) {

        Context context = new Context();
        context.setVariable("name", username);
        context.setVariable("jobs", value);

        String content = templateEngine.process(templateName, context);

    kafkaTemplate.send("confirm-account-topic", String.format("to=%s,subject=%s,content=%s",to,subject,content));

    }

}
