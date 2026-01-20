package com.example.demo.service;

import com.example.demo.dto.RateRequest;
import com.example.demo.model.Rate;
import com.example.demo.repository.RateRepository;
import org.springframework.stereotype.Service;

@Service
public class RateService {

    private final RateRepository rateRepository;

    public RateService(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    // Save rating (one per user)
    public Rate saveRate(RateRequest request) {

        if (rateRepository.findByUserEmail(request.getUserEmail()).isPresent()) {
            throw new RuntimeException("User already rated");
        }

        Rate rate = new Rate();
        rate.setUserEmail(request.getUserEmail());
        rate.setRating(request.getRating());
        rate.setComment(request.getComment());

        return rateRepository.save(rate);
    }
}
