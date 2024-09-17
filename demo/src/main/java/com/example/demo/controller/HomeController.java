package com.example.demo.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/home")
public class HomeController {
    // ...

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello, World!";
    }

    @RequestMapping
    public String home() {
        return "home";
    }

}