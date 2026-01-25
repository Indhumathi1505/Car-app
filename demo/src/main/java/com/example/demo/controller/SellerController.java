package com.example.demo.controller;

import com.example.demo.dto.SellerVerifyRequest;
import com.example.demo.model.Seller;
import com.example.demo.model.User;
import com.example.demo.repository.SellerRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;



import java.util.Map;

@RestController
@RequestMapping("/api/seller")
@CrossOrigin(origins = {"http://localhost:5173", "https://car-app-ch3s.onrender.com"}, allowCredentials = "true")
public class SellerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
private JwtUtil jwtUtil;


   @PostMapping("/verify")
public ResponseEntity<?> verifySeller(@RequestBody SellerVerifyRequest request) {

    // 1️⃣ Verify that user exists
    User user = userRepository.findByEmail(request.getEmail().toLowerCase())
            .orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED,
                            "User not found"
                    )
            );

    // 2️⃣ Verify password
    if (!user.getPassword().equals(request.getPassword())) {
        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid password"
        );
    }

    // 3️⃣ Check if seller already exists for this user
    Seller seller = sellerRepository.findByUser(user)
            .orElseGet(() -> {
                Seller newSeller = new Seller();
                newSeller.setUser(user);         
                newSeller.setEmail(user.getEmail());
                newSeller.setActive(true);
                return sellerRepository.save(newSeller);
            });

    // 4️⃣ Generate JWT with role
    String token = jwtUtil.generateToken(user.getEmail(), "SELLER");

    // 5️⃣ Return sellerId and token
    return ResponseEntity.ok(
        Map.of(
            "sellerId", seller.getId(),
            "token", token,
            "message", "Seller verified"
        )
    );
}
}

