package com.example.lazada_game.application;

import com.example.lazada_game.domain.model.Contact;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;


@Service
public class EmailService {

    private final SesClient sesClient;
    private final JavaMailSender mailSender;


    public EmailService(SesClient sesClient, JavaMailSender mailSender) {
        this.sesClient = sesClient;
        this.mailSender = mailSender;
    }


    public void sendOtpEmail(String toEmail, String otpCode) {
        try {
            Destination destination = Destination.builder()
                    .toAddresses(toEmail)
                    .build();

            Content subject = Content.builder()
                    .data("ลงทะเบียน 99 Lazadajury - การยืนยันอีเมล")
                    .charset("UTF-8")
                    .build();

            Content textBody = Content.builder()
                    .data("รหัส OTP ของคุณคือ " + otpCode + " (ใช้ได้ 5 นาที).")
                    .charset("UTF-8")
                    .build();

//            Content htmlBody = Content.builder()
//                    .data("<p>Your OTP is <b>" + otpCode + "</b> (valid 5 minutes).</p>")
//                    .charset("UTF-8")
//                    .build();

            Body body = Body.builder()
                    .text(textBody)
//                    .html(htmlBody)
                    .build();

            Message message = Message.builder()
                    .subject(subject)
                    .body(body)
                    .build();

            SendEmailRequest request = SendEmailRequest.builder()
                    .source("queries@digitalrouge.com") // email ที่ verify แล้ว
                    .destination(destination)
                    .message(message)
                    .configurationSetName("otp-99lazada") // 👈 แนบ ConfigSet ตรงนี้
                    .build();

            SendEmailResponse response = sesClient.sendEmail(request);
            System.out.println("Email sent with Message ID: " + response.messageId());

        } catch (SesException e) {
            System.err.println("Error sending email: " + e.awsErrorDetails().errorMessage());
        }
    }

    public void sendContactEmail(Contact contactRequest) {
        try {
            String description = contactRequest.getEmail() + " : " + contactRequest.getDescription();
            Destination destination = Destination.builder()
                    .toAddresses("queries@digitalrouge.com")
                    .build();

            Content subject = Content.builder()
                    .data(contactRequest.getTitle())
                    .charset("UTF-8")
                    .build();

            Content textBody = Content.builder()
                    .data(description)
                    .charset("UTF-8")
                    .build();

            Body body = Body.builder()
                    .text(textBody)
                    .build();

            Message message = Message.builder()
                    .subject(subject)
                    .body(body)
                    .build();

            SendEmailRequest request = SendEmailRequest.builder()
                    .source("queries@digitalrouge.com") // email ที่ verify แล้ว
                    .destination(destination)
                    .message(message)
                    .configurationSetName("otp-99lazada") // 👈 แนบ ConfigSet ตรงนี้
                    .build();

            SendEmailResponse response = sesClient.sendEmail(request);
            System.out.println("Contact : Email sent with Message ID: " + response.messageId());

        } catch (SesException e) {
            System.err.println("Error sending email: " + e.awsErrorDetails().errorMessage());
        }
    }

    public void sendEmailContact(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        String description = "Email : " + to + " : " + " Description : " + text;
        message.setFrom("apichatlasuk13@gmail.com");
        message.setTo("queries@digitalrouge.com");
        message.setSubject(subject);
        message.setText(description);
        mailSender.send(message);
    }
}