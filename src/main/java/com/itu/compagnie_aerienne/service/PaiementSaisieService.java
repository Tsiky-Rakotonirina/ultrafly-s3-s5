package com.itu.compagnie_aerienne.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itu.compagnie_aerienne.model.*;
import com.itu.compagnie_aerienne.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaiementSaisieService {
    
    private final PaiementRepository paiementRepository;
    private final PaiementDetailRepository paiementDetailRepository;
    private final PaiementModeRepository paiementModeRepository;
    private final DeviseRepository deviseRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationBilletRepository reservationBilletRepository;
    
    /**
     * Récupère un paiement par l'ID de la réservation
     */
    public Optional<Paiement> getPaiementByReservationId(Integer reservationId) {
        return paiementRepository.findByReservationIdReservation(reservationId);
    }
    
    /**
     * Récupère une réservation par son ID
     */
    public Optional<Reservation> getReservationById(Integer reservationId) {
        return reservationRepository.findById(reservationId);
    }
    
    /**
     * Récupère les billets d'une réservation
     */
    public List<ReservationBillet> getBilletsByReservationId(Integer reservationId) {
        return reservationBilletRepository.findByReservationIdReservationIn(List.of(reservationId));
    }
    
    /**
     * Récupère tous les modes de paiement
     */
    public List<PaiementMode> getAllPaiementModes() {
        return paiementModeRepository.findAll();
    }
    
    /**
     * Récupère toutes les devises
     */
    public List<Devise> getAllDevises() {
        return deviseRepository.findAll();
    }
    
    /**
     * Récupère les détails de paiement existants
     */
    public List<PaiementDetail> getPaiementDetails(Integer paiementId) {
        return paiementDetailRepository.findByPaiementIdPaiementIn(List.of(paiementId));
    }
    
    /**
     * Calcule le reste à payer
     */
    public BigDecimal calculerResteAPayer(Paiement paiement) {
        List<PaiementDetail> details = getPaiementDetails(paiement.getIdPaiement());
        BigDecimal totalPaye = details.stream()
            .map(PaiementDetail::getMontant)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return paiement.getMontantTotal().subtract(totalPaye);
    }
    
    /**
     * Effectue un paiement (partiel ou complet)
     */
    @Transactional
    public PaiementDetail effectuerPaiement(Integer paiementId, Integer modeId, Integer deviseId, BigDecimal montant) {
        Paiement paiement = paiementRepository.findById(paiementId)
            .orElseThrow(() -> new IllegalArgumentException("Paiement non trouvé"));
        
        PaiementMode mode = paiementModeRepository.findById(modeId)
            .orElseThrow(() -> new IllegalArgumentException("Mode de paiement non trouvé"));
        
        Devise devise = deviseRepository.findById(deviseId)
            .orElseThrow(() -> new IllegalArgumentException("Devise non trouvée"));
        
        BigDecimal resteAPayer = calculerResteAPayer(paiement);
        
        if (montant.compareTo(resteAPayer) > 0) {
            throw new IllegalArgumentException("Le montant dépasse le reste à payer (" + resteAPayer + ")");
        }
        
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0");
        }
        
        // Créer le détail de paiement
        PaiementDetail detail = new PaiementDetail();
        detail.setMontant(montant);
        detail.setDatePaiement(LocalDate.now());
        detail.setPaiementMode(mode);
        detail.setDevise(devise);
        detail.setPaiement(paiement);
        detail = paiementDetailRepository.save(detail);
        
        // Mettre à jour le reste à payer
        BigDecimal nouveauReste = resteAPayer.subtract(montant);
        paiement.setRestePayer(nouveauReste);
        paiementRepository.save(paiement);
        
        return detail;
    }
    
    /**
     * Vérifie si un paiement est complet
     */
    public boolean isPaiementComplet(Paiement paiement) {
        return calculerResteAPayer(paiement).compareTo(BigDecimal.ZERO) == 0;
    }
}
