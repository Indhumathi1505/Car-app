package com.example.demo.controller;

import com.example.demo.dto.ShowroomDTO;
import com.example.demo.model.Showroom;
import com.example.demo.repository.ShowroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/showroom")
@CrossOrigin(origins = "http://localhost:5173")
public class ShowroomController {

    @Autowired
    private ShowroomRepository showroomRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // =========================
    // SIGNUP
    // =========================
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody ShowroomDTO dto) {

        // Check if email already exists
        if (showroomRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(
                    java.util.Collections.singletonMap("message", "Email already exists"));
        }

        // Hash the password
        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        Showroom showroom = new Showroom();
        showroom.setName(dto.getName());
        showroom.setEmail(dto.getEmail());
        showroom.setPassword(hashedPassword); // store hashed password

        showroomRepository.save(showroom);

        return ResponseEntity.ok(java.util.Collections.singletonMap("message", "Signup successful"));
    }

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ShowroomDTO dto) {

        Optional<Showroom> opt = showroomRepository.findByEmail(dto.getEmail());
        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body(java.util.Collections.singletonMap("message", "Invalid email or password"));
        }

        Showroom showroom = opt.get();

        // Check password
        if (!passwordEncoder.matches(dto.getPassword(), showroom.getPassword())) {
            return ResponseEntity.status(401).body(java.util.Collections.singletonMap("message", "Invalid email or password"));
        }

        // Normally, you generate JWT token here
        String token = "dummy-token-" + showroom.getId(); // replace with real JWT if needed

        return ResponseEntity.ok(java.util.Collections.singletonMap("token", token));
    }

    // =========================
    // GOOGLE LOGIN
    // =========================
    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody ShowroomDTO dto) {

        Optional<Showroom> opt = showroomRepository.findByEmail(dto.getEmail());
        Showroom showroom;

        if (opt.isEmpty()) {
            // First-time Google login -> create showroom
            showroom = new Showroom();
            showroom.setName(dto.getName());
            showroom.setEmail(dto.getEmail());
            showroomRepository.save(showroom);
        } else {
            showroom = opt.get();
        }

        // Return token
        String token = "dummy-token-" + showroom.getId();
        return ResponseEntity.ok(java.util.Collections.singletonMap("token", token));
    }
}
