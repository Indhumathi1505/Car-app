package com.example.demo.service;

import com.example.demo.model.Showroom;
import com.example.demo.repository.ShowroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowroomService {

    @Autowired
    private ShowroomRepository showroomRepository;

    public Showroom saveShowroom(Showroom showroom) {
        return showroomRepository.save(showroom);
    }

    public List<Showroom> getAllShowrooms() {
        return showroomRepository.findAll();
    }

    public Showroom getShowroomByEmail(String email) {
        return showroomRepository
                .findByEmail(email)
                .orElse(null);
    }

    public void deleteShowroom(String id) {
        showroomRepository.deleteById(id);
    }
}
