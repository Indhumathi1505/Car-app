package com.example.demo.controller;

import com.example.demo.model.Car;
import com.example.demo.repository.CarRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.CarAdminResponse;
import java.util.Base64;
import java.util.stream.Collectors;

import java.util.List;

@RestController
@RequestMapping("/api/admin/cars")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminCarController {

    private final CarRepository carRepository;

    public AdminCarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

  
@GetMapping("/pending")
public ResponseEntity<List<CarAdminResponse>> getPendingCars() {

    List<CarAdminResponse> response = carRepository.findByApprovedFalse()
        .stream()
        .map(car -> {
            CarAdminResponse dto = new CarAdminResponse();

            dto.setId(car.getId());
            dto.setBrand(car.getBrand());
            dto.setModel(car.getModel());
            dto.setYear(car.getYear());
            dto.setPrice(car.getPrice());
            dto.setFuelType(car.getFuelType());
            dto.setCondition(car.getCondition());
            dto.setDescription(car.getDescription());

            // ✅ IMAGE BASE64
            if (car.getImage1() != null) {
                dto.setImage(
                    Base64.getEncoder().encodeToString(car.getImage1())
                );
            }

            // ✅ CERTIFICATE BASE64 (THIS WAS MISSING)
            if (car.getCertificate() != null) {
                dto.setCertificate(
                    Base64.getEncoder().encodeToString(car.getCertificate())
                );
            }

            return dto;
        })
        .collect(Collectors.toList());

    return ResponseEntity.ok(response);
}


    // 🔹 2. Approve a car
    @PutMapping("/approve/{id}")
    public ResponseEntity<?> approveCar(@PathVariable String id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        car.setApproved(true);
        carRepository.save(car);

        return ResponseEntity.ok("Car approved successfully");
    }

    // 🔹 3. Reject a car (delete OR mark rejected)
    @DeleteMapping("/reject/{id}")
    public ResponseEntity<?> rejectCar(@PathVariable String id) {
        if (!carRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        carRepository.deleteById(id);
        return ResponseEntity.ok("Car rejected and removed");
    }
}
