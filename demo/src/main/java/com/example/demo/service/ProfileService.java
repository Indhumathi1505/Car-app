package com.example.demo.service;

import com.example.demo.model.Profile;
import com.example.demo.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    /* ================= CREATE ================= */
    public Profile createProfile(
            String fullName,
            String email,
            String phone,
            String gender,
            String state,
            String city,
            String pincode,
            String address,
            MultipartFile image
    ) throws IOException {

        Profile profile = new Profile();
        profile.setFullName(fullName);
        profile.setEmail(email);
        profile.setPhone(phone);
        profile.setGender(gender);
        profile.setState(state);
        profile.setCity(city);
        profile.setPincode(pincode);
        profile.setAddress(address);

        if (image != null && !image.isEmpty()) {
            profile.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
        }

        return profileRepository.save(profile);
    }

    /* ================= FETCH BY EMAIL ================= */
    public Profile getProfileByEmail(String email) {
        return profileRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    /* ================= UPDATE (🔥 FIX) ================= */
    public Profile updateProfile(
            String email,
            String fullName,
            String phone,
            String gender,
            String city,
            MultipartFile image
    ) throws IOException {

        Profile profile = getProfileByEmail(email);

        profile.setFullName(fullName);
        profile.setPhone(phone);
        profile.setGender(gender);
        profile.setCity(city);

        if (image != null && !image.isEmpty()) {
            profile.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
        }

        return profileRepository.save(profile);
    }
}
