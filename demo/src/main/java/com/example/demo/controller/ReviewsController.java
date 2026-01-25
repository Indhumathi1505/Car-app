package com.example.demo.controller;

import com.example.demo.model.Car;
import com.example.demo.model.Review;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;
@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = {"http://localhost:5173", "https://car-app-ch3s.onrender.com"}) // adjust if needed
public class ReviewsController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CarRepository carRepository;

    private Optional<Car> carOpt;

    /**
     * GET all reviews for a specific car
     * URL: /api/reviews/car/{carId}
     */
    @GetMapping("/car/{carId}")
    public ResponseEntity<?> getReviewsByCarId(@PathVariable String carId) {
        try {
            // If car doesn't exist, return empty list (NOT 500)
            Optional<Car> carOpt = carRepository.findById(carId);
            if (!carOpt.isPresent()) {
                return ResponseEntity.ok(Collections.emptyList());
            }

            List<Review> reviews = reviewRepository.findByCarId(carId);
            return ResponseEntity.ok(reviews);

        } catch (Exception e) {
            e.printStackTrace(); // logs exact backend error
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching reviews");
        }
    }

    /**
     * POST add new review
     * URL: /api/reviews
     */
   @PostMapping
public ResponseEntity<?> addReview(@RequestBody Review review) {
    try {
        // 1. Validation
        if (review.getCarId() == null || review.getCarId().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "carId is required"));
        }
        if (review.getUserEmail() == null || review.getUserEmail().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "userEmail is required"));
        }
        if (review.getRating() < 1 || review.getRating() > 5) {
            return ResponseEntity.badRequest().body(Map.of("message", "rating must be between 1 and 5"));
        }

        // 2. Look up the car (local variable, not class field)
        Optional<Car> carExists = carRepository.findById(review.getCarId());
        if (!carExists.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Car not found with id: " + review.getCarId()));
        }

        // 3. Save the review
        Review savedReview = reviewRepository.save(review);

        // 4. Return SUCCESS (201 Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);

    } catch (Exception e) {
        e.printStackTrace(); 
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Internal Server Error: " + e.getMessage()));
    }
}
}