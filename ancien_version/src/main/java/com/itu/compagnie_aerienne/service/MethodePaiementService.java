package com.itu.compagnie_aerienne.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itu.compagnie_aerienne.model.MethodePaiement;
import com.itu.compagnie_aerienne.repository.MethodePaiementRepository;

@Service
public class MethodePaiementService {
    private final MethodePaiementRepository methodePaiementRepository;

    public MethodePaiementService(MethodePaiementRepository methodePaiementRepository) {
        this.methodePaiementRepository = methodePaiementRepository;
    }

    public List<MethodePaiement> getAllMethodesPaiement() {
        return methodePaiementRepository.findAll();
    }
}
