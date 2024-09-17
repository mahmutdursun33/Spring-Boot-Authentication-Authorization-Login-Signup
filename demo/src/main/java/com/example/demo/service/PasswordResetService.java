package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    // Şifre sıfırlama e-postasını gönderir
    public void sendResetPasswordEmail(String email) throws MessagingException {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Token oluştur
            String token = UUID.randomUUID().toString();

            // Token'ı veritabanına kaydet (kullanıcıya bağlı olarak bir token kaydetme işlemi)
            user.setResetToken(token);
            userRepository.save(user);

            // Şifre sıfırlama linki oluştur
            String resetLink = "http://localhost:8080/reset-password?token=" + token;

            // E-posta gönder
            sendEmail(user.getEmail(), resetLink);
        }
    }

    // HTML formatında e-posta gönderme işlemi
    private void sendEmail(String to, String resetLink) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        // HTML içeriğini yükle ve dinamik veriyi ekle
        Context context = new Context();
        context.setVariable("resetLink", resetLink);
        String htmlContent = templateEngine.process("reset-password-email", context); // HTML dosya adı

        helper.setTo(to);
        helper.setSubject("Şifre Sıfırlama Talebi");
        helper.setText(htmlContent, true); // ikinci parametre true ise HTML olarak gönderir
        mailSender.send(mimeMessage);
    }
}
