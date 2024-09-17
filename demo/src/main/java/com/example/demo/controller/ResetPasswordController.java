package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PasswordResetService;
import com.example.demo.service.UserDetaillServiceImpl;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class ResetPasswordController {

    @Autowired
    private UserDetaillServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Şifre sıfırlama isteği (forgot password)
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, Model model) throws MessagingException {
        // E-posta adresini veritabanından bul
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // E-posta doğrulaması yapılmışsa şifre sıfırlama linki gönder
            if (user.isEmailVerified()) {
                passwordResetService.sendResetPasswordEmail(email); // E-posta gönderimi
                model.addAttribute("message", "Reset link has been sent to your email.");
            } else {
                model.addAttribute("error", "Email is not verified.");
            }
        } else {
            model.addAttribute("error", "No account found with this email.");
        }

        return "forgot-password"; // Aynı sayfaya geri dön
    }

    // Şifre sıfırlama işlemi (POST metoduyla)
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token, @RequestParam("password") String newPassword, Model model) {
        Optional<User> userOptional = userService.findUserByResetToken(token);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword)); // Yeni şifreyi hashleyip ayarla
            user.setResetToken(null); // Token'ı geçersiz kıl
            userRepository.save(user); // Kullanıcıyı güncelle
            model.addAttribute("message", "Your password has been reset successfully.");
            return "login"; // Başarı mesajı ile giriş sayfasına yönlendir
        } else {
            model.addAttribute("error", "Invalid or expired token.");
            return "reset-password"; // Hata mesajı ile şifre sıfırlama sayfasına geri dön
        }
    }
}
