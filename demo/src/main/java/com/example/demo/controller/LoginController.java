package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserDetaillServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserDetaillServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password");
        }
        return "login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        // Token geçerliliğini kontrol et ve formu göster
        if (userService.findUserByResetToken(token).isPresent()) {
            model.addAttribute("token", token);
            return "reset-password";
        } else {
            model.addAttribute("error", "Invalid or expired token.");
            return "forgot-password";  // Token geçersizse şifre sıfırlama sayfasına dön
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model) {
        // Kullanıcıyı kullanıcı adı ile veritabanından bul
        User user = (User) userService.loadUserByUsername(username);

        // Eğer kullanıcı bulunamazsa, hata mesajı göster
        if (user == null) {
            model.addAttribute("errorMessage", "Kullanıcı bulunamadı.");
            return "login";  // Hatalı giriş, tekrar login sayfasına dön
        }

        // Şifreyi hash'lenmiş şifre ile karşılaştır
        if (!passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("errorMessage", "Şifre hatalı.");
            return "login";  // Hatalı giriş, tekrar login sayfasına dön
        }

        // Eğer e-posta doğrulanmamışsa, login işlemine izin verme
        if (!user.isEmailVerified()) {
            model.addAttribute("errorMessage", "E-posta doğrulaması yapılmamış. Lütfen e-posta adresinizi doğrulayın.");
            return "login";  // E-posta doğrulama eksik, login sayfasına geri dön
        }

        // Giriş başarılı, ana sayfaya yönlendir
        return "redirect:/home";
    }
}
