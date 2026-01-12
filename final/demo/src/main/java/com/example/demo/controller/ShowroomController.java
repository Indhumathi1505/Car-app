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

    if (dto.getEmail() == null || dto.getPassword() == null) {
        return ResponseEntity.badRequest()
                .body(java.util.Collections.singletonMap("message", "All fields required"));
    }

    if (showroomRepository.findByEmail(dto.getEmail()).isPresent()) {
        return ResponseEntity.badRequest()
                .body(java.util.Collections.singletonMap("message", "Email already exists"));
    }

    Showroom showroom = new Showroom();
    showroom.setName(dto.getName());
    showroom.setEmail(dto.getEmail().trim().toLowerCase());

    showroom.setPhone(dto.getPhone());
    showroom.setAddress(dto.getAddress());
    showroom.setPassword(passwordEncoder.encode(dto.getPassword()));

    showroomRepository.save(showroom);

    return ResponseEntity.ok(
            java.util.Collections.singletonMap("message", "Signup successful"));
}


    // =========================
    // LOGIN
    // =========================
// =========================
// LOGIN
// =========================
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody ShowroomDTO dto) {

    if (dto.getEmail() == null || dto.getPassword() == null) {
        return ResponseEntity.badRequest()
                .body(java.util.Collections.singletonMap("message", "All fields required"));
    }

   Optional<Showroom> opt =
    showroomRepository.findByEmail(dto.getEmail().trim().toLowerCase());


    if (opt.isEmpty()) {
        return ResponseEntity.status(401)
                .body(java.util.Collections.singletonMap("message", "Invalid email or password"));
    }

    Showroom showroom = opt.get();

    // check password
    if (!passwordEncoder.matches(dto.getPassword(), showroom.getPassword())) {
        return ResponseEntity.status(401)
                .body(java.util.Collections.singletonMap("message", "Invalid email or password"));
    }

    // success
    String token = "dummy-token-" + showroom.getEmail();

    return ResponseEntity.ok(
            java.util.Collections.singletonMap("token", token));
}



    // =========================
    // GOOGLE LOGIN
    // =========================
   @PostMapping("/google-login")
public ResponseEntity<?> googleLogin(@RequestBody ShowroomDTO dto) {

    Optional<Showroom> opt =
    showroomRepository.findByEmail(dto.getEmail().trim().toLowerCase());
    Showroom showroom;

    if (opt.isEmpty()) {
        showroom = new Showroom();
        showroom.setName(dto.getName());
        
showroom.setEmail(dto.getEmail().trim().toLowerCase());

        // 🔴 VERY IMPORTANT
        showroom.setPassword(passwordEncoder.encode("GOOGLE_USER"));

        showroomRepository.save(showroom);
    } else {
        showroom = opt.get();
    }

    String token = "dummy-token-" + showroom.getEmail();
    return ResponseEntity.ok(java.util.Collections.singletonMap("token", token));
}
}
