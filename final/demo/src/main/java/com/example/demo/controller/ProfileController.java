package com.example.demo.controller;

import com.example.demo.model.Profile;
import com.example.demo.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:3000")
public class ProfileController {

    @Autowired
    private ProfileRepository repository;

    // ✅ CREATE profile
    @PostMapping
    public Profile createProfile(@RequestBody Profile profile) {
        return repository.save(profile);
    }

    // ✅ GET profile (for display)
    @GetMapping
    public Profile getProfile() {
        return repository.findFirstByOrderByIdAsc()
                .orElse(null);
    }

    // ✅ UPDATE profile
    @PutMapping
    public Profile updateProfile(@RequestBody Profile profile) {
        return repository.save(profile); // Mongo uses _id to update
    }
}
