package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserDetaillServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

    @Autowired
    private UserDetaillServiceImpl userService;

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(@ModelAttribute User user, Model model) {
        if (!user.getPassword().equals(user.getPassword2())) {
            model.addAttribute("error", "Passwords do not match");
            return "signup";
        }
        userService.registerUser(user);
        return "redirect:/login";
    }
}