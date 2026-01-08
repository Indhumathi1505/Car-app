package com.example.demo.repository;

import com.example.demo.model.Seller;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import com.example.demo.model.User;

public interface SellerRepository extends MongoRepository<Seller, String> {
     Optional<Seller> findByUser(User user);


    Optional<Seller> findByEmail(String email);
}

