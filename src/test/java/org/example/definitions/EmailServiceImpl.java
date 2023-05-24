//package org.example.definitions;
//
//package io.factiiv.crm.app.email.service.implementation;
//
//import io.factiiv.crm.app.email.dto.EmailDTO;
//import io.factiiv.crm.app.email.service.EmailService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import javax.activation.DataHandler;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.util.ByteArrayDataSource;
//import java.io.File;
//import java.util.Objects;
//import java.util.concurrent.CompletableFuture;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class EmailServiceImpl implements EmailService {
//
//    private final JavaMailSenderImpl emailSender;
//
//    @Override
//    @Async
//    public CompletableFuture<ResponseEntity<?>> sendSimpleMail(EmailDTO emailDTO) {
//
//        try {
//            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//            simpleMailMessage.setFrom(emailSender.getUsername());
//            simpleMailMessage.setTo(emailDTO.getTo());
//            simpleMailMessage.setSubject(emailDTO.getSubject());
//            simpleMailMessage.setText(emailDTO.getText());
//            emailSender.send(simpleMailMessage);
////            return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.OK));
//        } catch (Exception e) {
//            log.error("EmailServiceImpl :: sendSimpleMail :: {}", e.getMessage());
////            return CompletableFuture.completedFuture(new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
//        }
//        return new CompletableFuture<>();
//    }
//
//    @Override
//    @Async
//    public CompletableFuture<ResponseEntity<?>> sendMessageWithAttachment(EmailDTO emailDTO) {
//        try {
//            MimeMessage message = emailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//            helper.setFrom(Objects.requireNonNull(emailSender.getUsername()));
//            helper.setTo(emailDTO.getTo());
//            helper.setSubject(emailDTO.getSubject());
//            helper.setText(emailDTO.getText());
//            if (emailDTO.getPathToAttachment() != null) {
//                FileSystemResource file = new FileSystemResource(new File(emailDTO.getPathToAttachment()));
//                helper.addAttachment(emailDTO.getAttachmentName(), file);
//
//            } else if (emailDTO.getAttachmentData() != null) {
//                ByteArrayDataSource file = new ByteArrayDataSource(emailDTO.getAttachmentData(), emailDTO.getAttachmentType());
//                MimeBodyPart attachmentBody = new MimeBodyPart();
//                attachmentBody.setDataHandler(new DataHandler(file));
//                helper.addAttachment(emailDTO.getAttachmentName(), attachmentBody.getDataHandler().getDataSource());
//            } else {
//                throw new RuntimeException("No data or path was set by request");
//            }
//            emailSender.send(message);
//            return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.OK));
//
//        } catch (Exception e) {
//            log.error("EmailServiceImpl :: sendMessageWithAttachment :: {}", e.getMessage());
//            return CompletableFuture.completedFuture(new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
//        }
//    }
//
//}