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

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5173") // adjust if needed
public class ReviewsController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CarRepository carRepository;

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
            // Validation
            if (review.getCarId() == null || review.getCarId().isEmpty()) {
                return ResponseEntity.badRequest().body("carId is required");
            }

            if (review.getUserEmail() == null || review.getUserEmail().isEmpty()) {
                return ResponseEntity.badRequest().body("userEmail is required");
            }

            if (review.getRating() < 1 || review.getRating() > 5) {
                return ResponseEntity.badRequest().body("rating must be between 1 and 5");
            }

            // Check if car exists
            Optional<Car> carOpt = carRepository.findById(review.getCarId());
            if (!carOpt.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Car not found with id: " + review.getCarId());
            }

            Review savedReview = reviewRepository.save(review);
            return ResponseEntity.ok(savedReview);

        } catch (Exception e) {
            e.printStackTrace(); // VERY IMPORTANT for debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to submit review");
        }
    }
}
