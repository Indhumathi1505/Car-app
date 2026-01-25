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
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://localhost:5174",
    "https://car-app-ch3s.onrender.com"
})

public class CarController {

    private final CarRepository carRepository;
    private final ShowroomRepository showroomRepository;
    private final JwtUtil jwtUtil;

   
     public CarController(CarRepository carRepository,
                         ShowroomRepository showroomRepository,
                         JwtUtil jwtUtil) {
        this.carRepository = carRepository;
        this.showroomRepository = showroomRepository;
        this.jwtUtil = jwtUtil;
    }

    // 🔹 Show only approved cars
    @GetMapping("/all")
    public List<Car> getAllCars() {
        return carRepository.findByApprovedTrue();
    }

    // Get all new cars
   @GetMapping("/new")
public List<Car> getNewCars() {
    return carRepository.findByConditionAndApprovedTrue("New");
}

@GetMapping("/used")
public List<Car> getUsedCars() {
    return carRepository.findByConditionAndApprovedTrue("Used");
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
   
    // ================= BRAND FILTER =================
@GetMapping("/brand/{brand}")
public List<Car> getCarsByBrand(@PathVariable String brand) {
    return carRepository.findByBrandRegexAndApproved(brand.trim());
}


@GetMapping("/{id}")
public ResponseEntity<Car> getCarById(@PathVariable String id) {
    return carRepository.findById(id)
        .filter(Car::getApproved)
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
      if (image != null && !image.isEmpty()) {
    car.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
}

       car.setCertificate(certificate.getBytes());

        car.setApproved(false);
        carRepository.save(car);

        return ResponseEntity.ok(car);
    }

    // ================= MAIN ADD CAR API =================
  /*
@PostMapping(value = "/add", consumes = "multipart/form-data")
public ResponseEntity<?> addCar(
        @RequestHeader(value = "Authorization", required = false) String authHeader,

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
        @RequestParam(required = false) List<String> features,
        @RequestParam MultipartFile image,
        @RequestParam(required = false) MultipartFile certificate
) {
    try {
        // 🔐 JWT validation
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing token");
        }

        String token = authHeader.substring(7);
       // String sellerEmail = jwtUtil.extractUsername(token);
      String sellerEmail = token.replace("dummy-token-", "").trim().toLowerCase();
       Optional<Showroom> showroomOpt =
        showroomRepository.findByEmail(sellerEmail);

if (showroomOpt.isEmpty()) {
    System.out.println("❌ SHOWROOM NOT FOUND FOR EMAIL: " + sellerEmail);
    return ResponseEntity.status(404).body("Showroom not found for this seller");
}




Showroom showroom = showroomOpt.get();
System.out.println("✅ SHOWROOM ID: " + showroom.getId());

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
        car.setApproved(false);
        car.setShowroomId(showroom.getId());
        car.setShowroomEmail(showroom.getEmail());


        if (features != null) {
            car.setFeatures(features);
        }

        // image
        car.setImage(Base64.getEncoder().encodeToString(image.getBytes()));

        // certificate
        if (certificate != null && !certificate.isEmpty()) {
            car.setCertificate(certificate.getBytes());
        }

        return ResponseEntity.ok(carRepository.save(car));

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Upload failed");
    }
}
*/
@PostMapping(value = "/add", consumes = "multipart/form-data")
public ResponseEntity<?> addCar(
        @RequestHeader(value = "Authorization", required = false) String authHeader,

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
        @RequestParam String sellerType,   // 🔥 USER or SHOWROOM
        @RequestParam(required = false) List<String> features,
        @RequestParam MultipartFile image,
        @RequestParam(required = false) MultipartFile certificate
) {
    try {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing token");
        }

        String token = authHeader.substring(7);
        String sellerEmail;

        // ========= USED CAR (USER) =========
        if (sellerType.equalsIgnoreCase("USER")) {

            sellerEmail = jwtUtil.extractUsername(token);

            Car car = new Car();
            car.setTitle(title);
            car.setBrand(brand.trim().toUpperCase());

            car.setBodyType(bodyType);
            car.setModel(model);
            car.setYear(year);
            car.setFuelType(fuelType);
            car.setMileage(mileage);
            car.setEngineCapacity(engineCapacity);
            car.setPrice(price);
            car.setDescription(description);
            car.setCondition("Used");
            car.setExteriorColor(exteriorColor);
            car.setSellerType("USER");
            car.setSellerEmail(sellerEmail);
            car.setApproved(false);

            car.setImage(Base64.getEncoder().encodeToString(image.getBytes()));

            if (certificate != null && !certificate.isEmpty()) {
                car.setCertificate(certificate.getBytes());
            }

            car.setFeatures(features);

            return ResponseEntity.ok(carRepository.save(car));
        }

        // ========= NEW CAR (SHOWROOM) =========
        else if (sellerType.equalsIgnoreCase("SHOWROOM")) {

            sellerEmail = token.replace("dummy-token-", "").trim().toLowerCase();

            Showroom showroom = showroomRepository.findByEmail(sellerEmail)
                    .orElseThrow(() -> new RuntimeException("Showroom not found"));

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
            car.setCondition("New");
            car.setExteriorColor(exteriorColor);
            car.setSellerType("SHOWROOM");
            car.setSellerEmail(sellerEmail);
            car.setShowroomId(showroom.getId());
            car.setShowroomEmail(showroom.getEmail());
            car.setApproved(false);

            car.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
            car.setFeatures(features);

            return ResponseEntity.ok(carRepository.save(car));
        }

        return ResponseEntity.badRequest().body("Invalid seller type");

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Upload failed");
    }
}


    // ================= ADMIN ENDPOINTS =================

    

    // ================= BRAND FILTER =================
 @GetMapping("/contact/{carId}")
public ResponseEntity<?> getShowroomContact(@PathVariable String carId) {

    Optional<Car> carOpt = carRepository.findById(carId);
    if (carOpt.isEmpty()) {
        return ResponseEntity.status(404).body("Car not found");
    }

    Car car = carOpt.get();

    if (car.getShowroomId() == null) {
        return ResponseEntity.status(404).body("Showroom ID missing in car");
    }

    Optional<Showroom> showroomOpt =
            showroomRepository.findById(car.getShowroomId());

    if (showroomOpt.isEmpty()) {
        return ResponseEntity.status(404).body("Showroom not found");
    }

    Showroom showroom = showroomOpt.get();

    ShowroomDTO dto = new ShowroomDTO();
    dto.setName(showroom.getName());
    dto.setEmail(showroom.getEmail());
    dto.setPhone(showroom.getPhone());
    dto.setAddress(showroom.getAddress());

    return ResponseEntity.ok(dto);
}
}
