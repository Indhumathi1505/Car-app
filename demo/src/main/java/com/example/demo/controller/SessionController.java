package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
public class SessionController {

    @GetMapping("/force-session")
    public String forceSession(HttpSession session) {
        return "Session ID: " + session.getId();
    }
}

