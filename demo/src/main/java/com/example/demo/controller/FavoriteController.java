package com.example.demo.controller;

import com.example.demo.dto.FavoriteRequest;
import com.example.demo.dto.FavoriteSummary;
import com.example.demo.service.FavoriteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:5173")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    // ✅ OPTION B – return minimal data
    @GetMapping("/{email}")
    public ResponseEntity<List<FavoriteSummary>> getFavorites(@PathVariable String email) {

        List<FavoriteSummary> favorites = favoriteService.getFavorites(email)
                .stream()
                .map(fav -> new FavoriteSummary(
                        fav.getId(),
                        fav.getCarId(),
                        fav.getCarType()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(favorites);
    }

    // Toggle favourite
    @PostMapping("/toggle")
    public ResponseEntity<Boolean> toggleFavorite(
            @RequestBody FavoriteRequest request) {

        boolean status = favoriteService.toggleFavorite(
                request.getUserEmail(),
                request.getCarId(),
                request.getCarType()
        );

        return ResponseEntity.ok(status);
    }

    // ✅ FIXED DELETE (String carId)
    @DeleteMapping("/{email}/{carId}")
    public ResponseEntity<Void> removeFavorite(
            @PathVariable String email,
            @PathVariable String carId) {

        favoriteService.remove(email, carId);
        return ResponseEntity.ok().build();
    }
}
