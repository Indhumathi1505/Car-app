package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")   // ✅ ADDED (IMPORTANT)
@CrossOrigin(
        origins = "http://localhost:5173",
        allowCredentials = "true"
)
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // =========================
    // NORMAL SIGNUP
    // =========================
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("{\"message\":\"Email already exists\"}");
        }

        userRepository.save(user);
        return ResponseEntity.ok("{\"message\":\"Signup successful\"}");
    }
    

    // =========================
    // NORMAL LOGIN
    // =========================
    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody User user, HttpSession session) {

    System.out.println("LOGIN REQUEST RECEIVED");
    System.out.println("Email: " + user.getEmail());

    Optional<User> existingUser =
            userRepository.findByEmail(user.getEmail());

    if (existingUser.isPresent()) {

        String dbPassword = existingUser.get().getPassword();
        String inputPassword = user.getPassword();

        if (dbPassword != null && dbPassword.equals(inputPassword)) {

            session.setAttribute("USER", existingUser.get());
            return ResponseEntity.ok("{\"message\":\"Login successful\"}");

        } else {
            return ResponseEntity
                    .status(401)
                    .body("{\"message\":\"Incorrect password\"}");
        }
    }

    return ResponseEntity
            .status(404)
            .body("{\"message\":\"User not found\"}");
}

   
    // =========================
    // GOOGLE LOGIN / SIGNUP
    // =========================
    @PostMapping("/google-login")
public ResponseEntity<?> googleLogin(@RequestBody User user, HttpSession session) {

    Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

    User sessionUser;

    if (existingUser.isEmpty()) {
        user.setPassword("");
        sessionUser = userRepository.save(user);
    } else {
        sessionUser = existingUser.get();
    }

    session.setAttribute("USER", sessionUser);

    return ResponseEntity.ok("{\"message\":\"Google login successful\"}");
}


    // =========================
    // CHECK CURRENT USER (COOKIE TEST)
    // =========================
    @GetMapping("/me")
    public ResponseEntity<?> currentUser(HttpSession session) {

        Object user = session.getAttribute("USER");

        if (user == null) {
            return ResponseEntity
                    .status(401)
                    .body("{\"message\":\"Not logged in\"}");
        }

        return ResponseEntity.ok(user);
    }
}
