package com.itu.compagnie_aerienne.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itu.compagnie_aerienne.model.Billet;
import com.itu.compagnie_aerienne.model.Reservation;
import com.itu.compagnie_aerienne.repository.BilletRepository;

@Service
public class BilletService {
    private final BilletRepository billetRepository;

    public BilletService(BilletRepository billetRepository) {
        this.billetRepository = billetRepository;
    }

    public List<Billet> getBilletsByReservation(Reservation reservation) {
        return billetRepository.findAll().stream()
                .filter(billet -> billet.getReservation().getId().equals(reservation.getId()))
                .toList();
    }

    public BigDecimal calculerMontantTotal(List<Billet> billets) {
        return billets.stream()
                .map(Billet::getPrix)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Billet> getBilletsByVolId(Integer volId) {
        return billetRepository.findAll().stream()
                .filter(billet -> billet.getSiegeVol().getVol().getId().equals(volId))
                .toList();
    }
}
