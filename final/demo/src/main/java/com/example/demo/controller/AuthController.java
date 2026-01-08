package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173") // React dev server
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // NORMAL SIGNUP
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("{\"message\":\"Email already exists\"}");
        }
        userRepository.save(user);
        return ResponseEntity.ok("{\"message\":\"Signup successful\"}");
    }

    // NORMAL LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            if (existingUser.get().getPassword().equals(user.getPassword())) {
                return ResponseEntity.ok("{\"message\":\"Login successful\"}");
            } else {
                return ResponseEntity.status(401).body("{\"message\":\"Incorrect password\"}");
            }
        } else {
            return ResponseEntity.status(404).body("{\"message\":\"User not found\"}");
        }
    }

    // GOOGLE LOGIN / SIGNUP
    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isEmpty()) {
            // Signup automatically if user doesn't exist
            user.setPassword(""); // No password needed for Google login
            userRepository.save(user);
        }
        return ResponseEntity.ok("{\"message\":\"Google login successful\"}");
    }
}  