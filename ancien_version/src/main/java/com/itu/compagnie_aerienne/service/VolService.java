package com.itu.compagnie_aerienne.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.itu.compagnie_aerienne.model.Vol;
import com.itu.compagnie_aerienne.repository.VolRepository;

@Service
public class VolService {
    private final VolRepository volRepository;

    public VolService(VolRepository volRepository) {
        this.volRepository = volRepository;
    }
    
    public List<Vol> getAllVols() {
        return volRepository.findAll();
    }

    public Optional<Vol> getVolById(Integer id) {
        return volRepository.findById(id);
    }

    public String reservation(){
        return "reservation";
    }
}
        