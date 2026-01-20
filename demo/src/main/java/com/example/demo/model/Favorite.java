package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "favorites")
public class Favorite {

    @Id
    private String id;

    private String userEmail;   // logged-in user
    private String carId;       // car _id
    private String carType;     // NEW or USED

    private LocalDateTime createdAt = LocalDateTime.now();

    // ===== Constructors =====
    public Favorite() {
    }

    public Favorite(String userEmail, String carId, String carType) {
        this.userEmail = userEmail;
        this.carId = carId;
        this.carType = carType;
        this.createdAt = LocalDateTime.now();
    }

    // ===== Getters & Setters =====
    public String getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
