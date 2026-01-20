package com.example.demo.repository;

import com.example.demo.model.Rate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RateRepository extends MongoRepository<Rate, String> {

    Optional<Rate> findByUserEmail(String userEmail);
}
