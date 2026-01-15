package com.itu.compagnie_aerienne.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itu.compagnie_aerienne.model.Billet;
import com.itu.compagnie_aerienne.model.Client;
import com.itu.compagnie_aerienne.model.Reservation;
import com.itu.compagnie_aerienne.model.SiegeVol;
import com.itu.compagnie_aerienne.model.Vol;
import com.itu.compagnie_aerienne.model.enums.StatutBillet;
import com.itu.compagnie_aerienne.model.enums.StatutReservation;
import com.itu.compagnie_aerienne.repository.BilletRepository;
import com.itu.compagnie_aerienne.repository.ReservationRepository;
import com.itu.compagnie_aerienne.repository.SiegeVolRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final BilletRepository billetRepository;
    private final SiegeVolRepository siegeVolRepository;
    private final PrixVolService prixVolService;

    public ReservationService(ReservationRepository reservationRepository,
                            BilletRepository billetRepository,
                            SiegeVolRepository siegeVolRepository,
                        PrixVolService prixVolService) {
        this.reservationRepository = reservationRepository;
        this.billetRepository = billetRepository;
        this.siegeVolRepository = siegeVolRepository;
        this.prixVolService = prixVolService;
    }

    @Transactional
    public Reservation creerReservation(Vol vol, Client client, List<SiegeVol> siegesVol) {
        // Vérifier que tous les sièges sont disponibles
        for (SiegeVol siegeVol : siegesVol) {
            if (siegeVol.getOccupe()) {
                throw new IllegalStateException("Le siège " + siegeVol.getSiege().getNumeroSiege() + " est déjà occupé");
            }
        }

        // Créer la réservation
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setVol(vol);
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setStatut(StatutReservation.CONFIRMEE);
        reservation = reservationRepository.save(reservation);

        // Créer les billets et marquer les sièges comme occupés
        for (SiegeVol siegeVol : siegesVol) {
            Billet billet = new Billet();
            billet.setReservation(reservation);
            billet.setSiegeVol(siegeVol);
            billet.setPrix(prixVolService.getPrixForSiegeVol(siegeVol)); // À calculer selon PrixVol
            billet.setStatut(StatutBillet.EMIS);
            billetRepository.save(billet);

            // Marquer le siège comme occupé
            siegeVol.setOccupe(true);
            siegeVolRepository.save(siegeVol);
        }

        return reservation;
    }

    public String payement(){
        return "payement";
    }
}
