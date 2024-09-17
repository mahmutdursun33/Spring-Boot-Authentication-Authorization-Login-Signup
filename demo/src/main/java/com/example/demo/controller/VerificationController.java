package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;


@Controller
public class VerificationController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token) {
        User user = userRepository.findByVerificationToken(token);
        if (user == null) {
            return "redirect:/error?message=Invalid token"; // Hatalı token
        }
        LocalDateTime tokenCreationTime = user.getVerificationTokenCreatedAt();
        LocalDateTime currentTime = LocalDateTime.now();

        // Token 1 dakikadan eskiyse geçersiz kıl
        if (tokenCreationTime.plusMinutes(1).isBefore(currentTime)) {
            userRepository.delete(user); // Kullanıcıyı sil
            return "redirect:/error?message=Token expired"; // Token süresi dolmuş
        }

        user.setVerificationTokenCreatedAt(null); // Token oluşturulma zamanını sıfırla
        user.setEmailVerified(true);
        userRepository.save(user);
        return "redirect:/login"; // Doğrulama başarılı
    }
}
