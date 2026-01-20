package com.example.demo.dto;

public class FavoriteSummary {

    private String id;
    private String carId;
    private String carType;

    public FavoriteSummary(String id, String carId, String carType) {
        this.id = id;
        this.carId = carId;
        this.carType = carType;
    }

    public String getId() {
        return id;
    }

    public String getCarId() {
        return carId;
    }

    public String getCarType() {
        return carType;
    }
}
