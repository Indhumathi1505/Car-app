package com.example.demo.service;

import com.example.demo.model.Favorite;
import com.example.demo.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    public List<Favorite> getFavorites(String email) {
        return favoriteRepository.findByUserEmail(email);
    }

    public boolean toggleFavorite(String email, String carId, String carType) {

        return favoriteRepository
                .findByUserEmailAndCarId(email, carId)
                .map(existing -> {
                    favoriteRepository.delete(existing);
                    return false;
                })
                .orElseGet(() -> {
                    favoriteRepository.save(
                            new Favorite(email, carId, carType)
                    );
                    return true;
                });
    }

    // ✅ REMOVE FIX
    public void remove(String email, String carId) {
        favoriteRepository
                .findByUserEmailAndCarId(email, carId)
                .ifPresent(favoriteRepository::delete);
    }
}
