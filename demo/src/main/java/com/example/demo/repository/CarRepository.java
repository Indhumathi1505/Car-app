package com.example.demo.repository;

import com.example.demo.model.Car;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.mongodb.repository.Query;

@Repository
public interface CarRepository extends MongoRepository<Car, String> {
    List<Car> findByCondition(String condition);

    // Public cars
    List<Car> findByApprovedTrue();

    // Admin approval list
    List<Car> findByApprovedFalse();

    // Showroom specific cars
    List<Car> findByShowroomEmail(String showroomEmail);
     List<Car> findBySellerEmail(String sellerEmail);
     // ✅ BRAND FILTER (ONLY APPROVED CARS)
     @Query("{ 'brand': { $regex: ?0, $options: 'i' }, 'approved': true }")
List<Car> findByBrandRegexAndApproved(String brand);

List<Car> findByApprovedTrueAndConditionIn(List<String> conditions);
List<Car> findByConditionAndApprovedTrue(String condition);



}
