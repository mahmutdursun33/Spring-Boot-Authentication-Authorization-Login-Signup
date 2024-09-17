package com.example.demo.service;

import com.example.demo.entity.PasswordResetToken;
import com.example.demo.entity.User;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserDetaillServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if (!user.isEmailVerified()) {
            throw new UsernameNotFoundException("E-posta doğrulaması yapılmamış.");
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

    public void registerUser(User user) {
        validatePassword(user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRole("USER");
        String token = UUID.randomUUID().toString();
        user.setVerificationTokenCreatedAt(LocalDateTime.now());
        user.setVerificationToken(token);
        userRepository.save(user);

        try {
            emailService.sendVerificationEmail(user.getEmail(), token);
        } catch (MessagingException e) {
            // Log the error
            System.err.println("E-posta gönderimi sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    private void validatePassword(String password) {
        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        Pattern lowerCasePattern = Pattern.compile("[a-z]");
        Pattern numberPattern = Pattern.compile("[0-9]");
        Pattern specialCharPattern = Pattern.compile("[@$!%*?.&]");

        if (!upperCasePattern.matcher(password).find() ||
                !lowerCasePattern.matcher(password).find() ||
                !numberPattern.matcher(password).find() ||
                !specialCharPattern.matcher(password).find()) {
            throw new IllegalArgumentException("Password must include uppercase, lowercase, number, and special character.");
        }
    }

    public boolean resetPassword(String token, String newPassword) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null); // Token'ı sıfırla
            userRepository.save(user);

            // Silinen token'ları temizle (opsiyonel, eğer bir token repository'niz varsa)
            tokenRepository.deleteByToken(token);

            return true;
        }
        return false;
    }

    public Optional<User> findUserByResetToken(String token) {
        return userRepository.findByResetToken(token);
    }
}

