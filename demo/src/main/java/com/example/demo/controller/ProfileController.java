package com.example.demo.controller;

import com.example.demo.model.Profile;
import com.example.demo.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = {"http://localhost:3000", "https://car-app-ch3s.onrender.com"})
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    // 🔹 CREATE PROFILE (USED BY info.jsx)
    @PostMapping
    public ResponseEntity<Profile> createProfile(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String gender,
            @RequestParam String state,
            @RequestParam String city,
            @RequestParam String pincode,
            @RequestParam String address,
            @RequestParam(required = false) MultipartFile image
    ) throws IOException {

        Profile savedProfile = profileService.createProfile(
                fullName, email, phone, gender, state, city, pincode, address, image
        );

        return ResponseEntity.ok(savedProfile);
    }

    // 🔹 FETCH PROFILE
    @GetMapping("/{email}")
    public ResponseEntity<Profile> getProfile(@PathVariable String email) {
        return ResponseEntity.ok(profileService.getProfileByEmail(email));
    }
}
