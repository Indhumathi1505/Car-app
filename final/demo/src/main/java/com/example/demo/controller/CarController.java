package com.example.demo.controller;

import com.example.demo.dto.ShowroomDTO;
import com.example.demo.model.Car;
import com.example.demo.model.Showroom;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.ShowroomRepository;
import com.example.demo.security.JwtUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Collections;
import java.util.Arrays;
import java.util.Optional;


@RestController
@RequestMapping("/api/cars")
@CrossOrigin(origins = "http://localhost:5173")
public class CarController {

 private final CarRepository carRepository;
    private final ShowroomRepository showroomRepository;

   
     public CarController(CarRepository carRepository,
                         ShowroomRepository showroomRepository) {
        this.carRepository = carRepository;
        this.showroomRepository = showroomRepository;
    }

    // 🔹 Show only approved cars
    @GetMapping("/all")
    public List<Car> getAllCars() {
        return carRepository.findByApprovedTrue();
    }

    // Get all new cars
    @GetMapping("/new")
    public List<Car> getNewCars() {
        return carRepository.findByCondition("New");
    }
    @GetMapping("/recommended")
public List<Car> getRecommendedCars() {

    // 1️⃣ Get both New + Used approved cars
    List<Car> cars = carRepository.findByApprovedTrueAndConditionIn(
            Arrays.asList("New", "Used")
    );

    // 2️⃣ Shuffle (random order)
    Collections.shuffle(cars);

    // 3️⃣ Return only first 6 cars
    return cars.stream().limit(6).toList();
}

    // Get all used cars
    @GetMapping("/used")
    public List<Car> getUsedCars() {
        return carRepository.findByCondition("Used");
    }
    // ================= BRAND FILTER =================
@GetMapping("/brand/{brand}")
public List<Car> getCarsByBrand(@PathVariable String brand) {
    return carRepository.findByBrandIgnoreCaseAndApprovedTrue(brand);
}

   @GetMapping("/{id}")
public ResponseEntity<Car> getCarById(@PathVariable String id) {
    return carRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}


   

    // ================= BASIC ADD =================
    @PostMapping("/add-basic")
    public ResponseEntity<?> addCarBasic(
            @RequestParam("image") MultipartFile image,
            @RequestParam("certificate") MultipartFile certificate,
            @ModelAttribute Car car
    ) throws IOException {

        // ✅ STRING ONLY (Base64)
        car.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
       car.setCertificate(certificate.getBytes());

        car.setApproved(false);
        carRepository.save(car);

        return ResponseEntity.ok(car);
    }

    // ================= MAIN ADD CAR API =================
    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<?> addCar(
            @RequestParam String title,
            @RequestParam String brand,  
            @RequestParam String bodyType,
            @RequestParam String model,
            @RequestParam Integer year,
            @RequestParam String fuelType,
            @RequestParam Integer mileage,
            @RequestParam Integer engineCapacity,
            @RequestParam Double price,
            @RequestParam String description,
            @RequestParam String condition,
            @RequestParam String exteriorColor,
             @RequestParam String sellerType,
           @RequestParam String sellerEmail,
            @RequestParam(required = false) List<String> features,
            @RequestParam MultipartFile image,
            @RequestParam(required = false) MultipartFile certificate,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {

        try {
            Car car = new Car();
            car.setTitle(title);
            car.setBrand(brand);
            car.setBodyType(bodyType);
            car.setModel(model);
            car.setYear(year);
            car.setFuelType(fuelType);
            car.setMileage(mileage);
            car.setEngineCapacity(engineCapacity);
            car.setPrice(price);
            car.setDescription(description);
            car.setCondition(condition);
            car.setExteriorColor(exteriorColor);
            car.setSellerType(sellerType);
              car.setSellerEmail(sellerEmail);

            if (features != null) {
                car.setFeatures(features);
            }

            // ✅ STRING ONLY (Base64)
            car.setImage(Base64.getEncoder().encodeToString(image.getBytes()));

           

if (certificate != null && !certificate.isEmpty()) {
    car.setCertificate(certificate.getBytes());
}

           
           

            // 🔐 Showroom logic (UNCHANGED)
            if ("SHOWROOM".equalsIgnoreCase(sellerType)) {
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    return ResponseEntity.status(401).body("Showroom login required");
                }
                String email = jwtUtil.extractUsername(authHeader.substring(7));
                car.setShowroomEmail(email);
                car.setApproved(false);
            } else {
                car.setApproved(false);
            }

            return ResponseEntity.ok(carRepository.save(car));

        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed");
        }
    }

    // ================= ADMIN ENDPOINTS =================

    @GetMapping("/pending")
    public List<Car> getPendingCars() {
        return carRepository.findByApprovedFalse();
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<?> approveCar(@PathVariable String id) {
        return carRepository.findById(id).map(car -> {
            car.setApproved(true);
            carRepository.save(car);
            return ResponseEntity.ok(car);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<?> rejectCar(@PathVariable String id) {
        return carRepository.findById(id).map(car -> {
            carRepository.delete(car);
            return ResponseEntity.ok("Car rejected and deleted");
        }).orElse(ResponseEntity.notFound().build());
    }
    // ================= BRAND FILTER =================
  
    @GetMapping("/contact/{carId}")
public ResponseEntity<?> getShowroomContact(@PathVariable String carId) {

    Optional<Car> carOpt = carRepository.findById(carId);

    if (carOpt.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    Car car = carOpt.get();

    if (car.getShowroomEmail() == null) {
        return ResponseEntity.badRequest()
                .body(java.util.Collections.singletonMap(
                        "message", "No showroom linked to this car"));
    }

    Optional<Showroom> showroomOpt =
            showroomRepository.findByEmail(car.getShowroomEmail());

    if (showroomOpt.isEmpty()) {
        return ResponseEntity.notFound()
                .build();
    }

    Showroom showroom = showroomOpt.get();

    // 🚫 DO NOT SEND PASSWORD
    ShowroomDTO dto = new ShowroomDTO();
    dto.setName(showroom.getName());
    dto.setEmail(showroom.getEmail());
    dto.setPhone(showroom.getPhone());
    dto.setAddress(showroom.getAddress());

    return ResponseEntity.ok(dto);
}



}
