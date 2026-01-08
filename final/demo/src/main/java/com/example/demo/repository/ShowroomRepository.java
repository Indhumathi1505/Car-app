package com.example.demo.repository;

import com.example.demo.model.Showroom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShowroomRepository extends MongoRepository<Showroom, String> {
    Optional<Showroom> findByEmail(String email);
}
