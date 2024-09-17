package com.example.demo.entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Transient
    private String password2;// Geçici şifre alanı, veritabanında saklanmaz
    private String role;
    private String email; // E-posta adresi eklenmiş
    private boolean isEmailVerified = false;
    private String verificationToken; // Kullanıcıya gönderilen token
    private LocalDateTime verificationTokenCreatedAt; // Token'ın oluşturulma zamanı
    private String resetToken; // Şifre sıfırlama token'ı

    public void setVerificationTokenCreatedAt(LocalDateTime verificationTokenCreatedAt) {
        this.verificationTokenCreatedAt = verificationTokenCreatedAt;
    }

    public LocalDateTime getVerificationTokenCreatedAt() {
        return verificationTokenCreatedAt;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }
    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }


    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getResetToken() {
        return resetToken;
    }
}