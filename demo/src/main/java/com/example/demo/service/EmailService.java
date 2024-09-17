package com.example.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendVerificationEmail(String to, String token) throws MessagingException {
        String verificationUrl = "http://localhost:8080/verify-email?token=" + token;

        // Thymeleaf şablonunu kullanarak e-posta içeriğini oluştur
        Context context = new Context();
        context.setVariable("verificationUrl", verificationUrl);
        String htmlContent = templateEngine.process("verification-email", context);

        // E-posta gönderimi
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
        messageHelper.setTo(to);
        messageHelper.setSubject("E-posta Doğrulama");
        messageHelper.setText(htmlContent, true); // HTML içeriği olarak gönder

        mailSender.send(mimeMessage);
    }

}
