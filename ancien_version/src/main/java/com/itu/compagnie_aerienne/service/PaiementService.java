package com.itu.compagnie_aerienne.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itu.compagnie_aerienne.model.Paiement;
import com.itu.compagnie_aerienne.model.PaiementMethode;
import com.itu.compagnie_aerienne.model.Reservation;
import com.itu.compagnie_aerienne.model.enums.StatutPaiement;
import com.itu.compagnie_aerienne.repository.PaiementMethodeRepository;
import com.itu.compagnie_aerienne.repository.PaiementRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final PaiementMethodeRepository paiementMethodeRepository;

    public Paiement creerPaiement(Reservation reservation, BigDecimal montant) {
        Paiement paiement = new Paiement();
        paiement.setReservation(reservation);
        paiement.setMontant(montant);
        paiement.setStatut(StatutPaiement.EN_ATTENTE);
        return paiementRepository.save(paiement);
    }

    public BigDecimal calculerResteAPayer(Paiement paiement) {
        List<PaiementMethode> paiementMethodes = paiementMethodeRepository.findAll()
            .stream()
            .filter(pm -> pm.getPaiement().getId().equals(paiement.getId()))
            .toList();
        
        BigDecimal montantDejaPaye = paiementMethodes.stream()
            .map(PaiementMethode::getMontant)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return paiement.getMontant().subtract(montantDejaPaye);
    }
}
