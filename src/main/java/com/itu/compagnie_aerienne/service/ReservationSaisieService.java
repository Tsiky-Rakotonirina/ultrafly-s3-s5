package com.itu.compagnie_aerienne.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itu.compagnie_aerienne.model.*;
import com.itu.compagnie_aerienne.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationSaisieService {
    
    private final VolRepository volRepository;
    private final ClientRepository clientRepository;
    private final AvionSiegeRepository avionSiegeRepository;
    private final VolTarrifRepository volTarrifRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationBilletRepository reservationBilletRepository;
    private final ReservationStatutRepository reservationStatutRepository;
    private final BilletStatutRepository billetStatutRepository;
    private final VolDetailRepository volDetailRepository;
    private final PaiementRepository paiementRepository;
    
    /**
     * Récupère un vol par son ID
     */
    public Optional<Vol> getVolById(Integer volId) {
        return volRepository.findById(volId);
    }
    
    /**
     * Récupère tous les clients
     */
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    
    /**
     * Récupère l'avion associé à un vol via VolDetail
     */
    public Avion getAvionByVolId(Integer volId) {
        List<VolDetail> details = volDetailRepository.findByVolIdVol(volId);
        if (!details.isEmpty()) {
            return details.get(0).getAvion();
        }
        return null;
    }
    
    /**
     * Récupère tous les sièges d'un avion
     */
    public List<AvionSiege> getSiegesByAvionId(Integer avionId) {
        return avionSiegeRepository.findByAvionIdAvion(avionId);
    }
    
    /**
     * Récupère les sièges déjà réservés pour un vol
     */
    public List<Integer> getSiegesReservesIds(Integer volId) {
        List<Reservation> reservations = reservationRepository.findAllByVolIdVol(volId);
        List<Integer> reservationIds = reservations.stream()
            .map(Reservation::getIdReservation)
            .toList();
        
        if (reservationIds.isEmpty()) {
            return List.of();
        }
        
        List<ReservationBillet> billets = reservationBilletRepository.findByReservationIdReservationIn(reservationIds);
        return billets.stream()
            .map(b -> b.getAvionSiege().getIdAvionSiege())
            .toList();
    }
    
    /**
     * Récupère les tarifs par catégorie de siège pour un vol
     */
    public Map<Integer, BigDecimal> getTarifsByCategorie(Integer volId) {
        List<VolTarrif> tarifs = volTarrifRepository.findAllByVolIdVol(volId);
        Map<Integer, BigDecimal> tarifMap = new HashMap<>();
        for (VolTarrif t : tarifs) {
            if (t != null && t.getSiegeCategorie() != null && t.getPrix() != null) {
                tarifMap.put(t.getSiegeCategorie().getIdSiegeCategorie(), t.getPrix());
            }
        }
        return tarifMap;
    }
    
    /**
     * Crée une réservation avec les sièges sélectionnés
     */
    @Transactional
    public Reservation creerReservation(Integer volId, Integer clientId, List<Integer> siegeIds) {
        Vol vol = volRepository.findById(volId)
            .orElseThrow(() -> new IllegalArgumentException("Vol non trouvé"));
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new IllegalArgumentException("Client non trouvé"));
        
        // Vérifier que les sièges ne sont pas déjà réservés
        List<Integer> siegesReserves = getSiegesReservesIds(volId);
        for (Integer siegeId : siegeIds) {
            if (siegesReserves.contains(siegeId)) {
                throw new IllegalStateException("Le siège " + siegeId + " est déjà réservé");
            }
        }
        
        // Récupérer le statut "En attente" ou le premier disponible
        ReservationStatut statut = reservationStatutRepository.findAll().stream()
            .filter(s -> s.getLibelle().toLowerCase().contains("attente"))
            .findFirst()
            .orElse(reservationStatutRepository.findAll().get(0));
        
        // Créer la réservation (le numero est généré par trigger PostgreSQL)
        Reservation reservation = new Reservation();
        reservation.setDateReservation(LocalDate.now());
        reservation.setClient(client);
        reservation.setVol(vol);
        reservation.setReservationStatut(statut);
        reservation = reservationRepository.saveAndFlush(reservation);
        
        // Récupérer le statut billet "Emis" ou le premier disponible
        BilletStatut billetStatut = billetStatutRepository.findAll().stream()
            .filter(s -> s.getLibelle().toLowerCase().contains("emis"))
            .findFirst()
            .orElse(billetStatutRepository.findAll().get(0));
        
        // Récupérer les tarifs
        Map<Integer, BigDecimal> tarifs = getTarifsByCategorie(volId);
        
        // Calculer le montant total
        BigDecimal montantTotal = BigDecimal.ZERO;
        
        // Créer les billets pour chaque siège (numero généré par trigger)
        for (Integer siegeId : siegeIds) {
            AvionSiege siege = avionSiegeRepository.findById(siegeId)
                .orElseThrow(() -> new IllegalArgumentException("Siège non trouvé: " + siegeId));
            
            BigDecimal prix = tarifs.getOrDefault(siege.getSiegeCategorie().getIdSiegeCategorie(), BigDecimal.ZERO);
            montantTotal = montantTotal.add(prix);
            
            ReservationBillet billet = new ReservationBillet();
            billet.setPrix(prix);
            billet.setAvionSiege(siege);
            billet.setReservation(reservation);
            billet.setBilletStatut(billetStatut);
            reservationBilletRepository.saveAndFlush(billet);
        }
        
        // Créer le paiement associé (numero généré par trigger)
        Paiement paiement = new Paiement();
        paiement.setMontantTotal(montantTotal);
        paiement.setRestePayer(montantTotal);
        paiement.setReservation(reservation);
        paiementRepository.saveAndFlush(paiement);
        
        return reservation;
    }
}
