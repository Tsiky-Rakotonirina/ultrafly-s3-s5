package com.itu.compagnie_aerienne.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itu.compagnie_aerienne.model.ClasseSiege;
import com.itu.compagnie_aerienne.repository.ClasseSiegeRepository;

@Service
public class ClasseSiegeService {
    private final ClasseSiegeRepository classeSiegeRepository;

    public ClasseSiegeService(ClasseSiegeRepository classeSiegeRepository) {
        this.classeSiegeRepository = classeSiegeRepository;
    }

    public List<ClasseSiege> getAllClasseSieges() {
        return classeSiegeRepository.findAll();
    }
}
