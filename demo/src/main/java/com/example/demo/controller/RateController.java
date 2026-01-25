package com.example.demo.controller;

import com.example.demo.dto.RateRequest;
import com.example.demo.model.Rate;
import com.example.demo.service.RateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rate")
@CrossOrigin(origins = {"http://localhost:5173", "https://car-app-ch3s.onrender.com"})
public class RateController {

    private final RateService rateService;

    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    @PostMapping
    public ResponseEntity<?> submitRate(@RequestBody RateRequest request) {
        return ResponseEntity.ok(rateService.saveRate(request));
    }
}
