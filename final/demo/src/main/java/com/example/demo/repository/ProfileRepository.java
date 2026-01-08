package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.model.Profile;

import java.util.Optional;

public interface ProfileRepository extends MongoRepository<Profile, String> {

    Optional<Profile> findFirstByOrderByIdAsc();
}
