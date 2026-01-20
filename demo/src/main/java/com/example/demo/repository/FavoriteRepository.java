package com.example.demo.repository;

import com.example.demo.model.Favorite;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends MongoRepository<Favorite, String> {

    List<Favorite> findByUserEmail(String userEmail);

    Optional<Favorite> findByUserEmailAndCarId(String userEmail, String carId);
}
