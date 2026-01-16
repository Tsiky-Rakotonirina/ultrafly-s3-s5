package com.itu.compagnie_aerienne.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itu.compagnie_aerienne.model.Avion;
import com.itu.compagnie_aerienne.model.AvionSiege;
import com.itu.compagnie_aerienne.model.BilletStatut;
import com.itu.compagnie_aerienne.model.Client;
import com.itu.compagnie_aerienne.model.Paiement;
import com.itu.compagnie_aerienne.model.Reservation;
import com.itu.compagnie_aerienne.model.ReservationBillet;
import com.itu.compagnie_aerienne.model.ReservationStatut;
import com.itu.compagnie_aerienne.model.Vol;
import com.itu.compagnie_aerienne.model.VolDetail;
import com.itu.compagnie_aerienne.model.VolTarrif;
import com.itu.compagnie_aerienne.model.VolTarrifRemise;
import com.itu.compagnie_aerienne.repository.AvionSiegeRepository;
import com.itu.compagnie_aerienne.repository.BilletStatutRepository;
import com.itu.compagnie_aerienne.repository.ClientRepository;
import com.itu.compagnie_aerienne.repository.PaiementRepository;
import com.itu.compagnie_aerienne.repository.ReservationBilletRepository;
import com.itu.compagnie_aerienne.repository.ReservationRepository;
import com.itu.compagnie_aerienne.repository.ReservationStatutRepository;
import com.itu.compagnie_aerienne.repository.VolDetailRepository;
import com.itu.compagnie_aerienne.repository.VolRepository;
import com.itu.compagnie_aerienne.repository.VolTarrifRemiseRepository;
import com.itu.compagnie_aerienne.repository.VolTarrifRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationSaisieService {
    
    private final VolRepository volRepository;
    private final ClientRepository clientRepository;
    private final AvionSiegeRepository avionSiegeRepository;
    private final VolTarrifRepository volTarrifRepository;
    private final VolTarrifRemiseRepository volTarrifRemiseRepository;
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
     * Récupère les remises de tarifs (vol_tarrif_remise) pour le vol
     * Retourne une structure: {categorieId: {clientTypeId: prixReduit}}
     */
    public Map<String, Object> getTarifRemises(Integer volId) {
        List<VolTarrif> tarifs = volTarrifRepository.findAllByVolIdVol(volId);
        Map<String, Object> result = new HashMap<>();
        
        // Structure: categorieId -> Map(clientTypeId -> prix)
        for (VolTarrif tarrif : tarifs) {
            Integer categorieId = tarrif.getSiegeCategorie().getIdSiegeCategorie();
            
            // Récupérer les remises pour ce tarif
            List<VolTarrifRemise> remises = volTarrifRemiseRepository.findByVolTarrifIdVolTarrif(tarrif.getIdVolTarrif());
            
            if (!remises.isEmpty()) {
                Map<Integer, BigDecimal> remisesParClientType = new HashMap<>();
                for (VolTarrifRemise remise : remises) {
                    // Stocker le prix réduit par type de client
                    if (remise.getClientType() != null) {
                        remisesParClientType.put(remise.getClientType().getIdClientType(), remise.getPrix());
                    }
                }
                if (!remisesParClientType.isEmpty()) {
                    result.put(categorieId.toString(), remisesParClientType);
                }
            }
        }
        
        return result;
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
    
    /**
     * Crée une réservation avec les clients et prix spécifiques pour chaque billet
     * @param volId ID du vol
     * @param clientPrincipalId ID du client principal responsable de la réservation
     * @param siegesData Map contenant pour chaque siège: {clientId, prix}
     */
    @Transactional
    public Reservation creerReservationAvecClients(Integer volId, Integer clientPrincipalId, Map<Integer, Map<String, Object>> siegesData) {
        Vol vol = volRepository.findById(volId)
            .orElseThrow(() -> new IllegalArgumentException("Vol non trouvé"));
        
        // Récupérer le client principal
        Client clientPrincipal = clientRepository.findById(clientPrincipalId)
            .orElseThrow(() -> new IllegalArgumentException("Client principal non trouvé"));
        
        // Vérifier que les sièges ne sont pas déjà réservés
        List<Integer> siegesReserves = getSiegesReservesIds(volId);
        for (Integer siegeId : siegesData.keySet()) {
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
        reservation.setClient(clientPrincipal);
        reservation.setVol(vol);
        reservation.setReservationStatut(statut);
        reservation = reservationRepository.saveAndFlush(reservation);
        
        // Récupérer le statut billet "Emis" ou le premier disponible
        BilletStatut billetStatut = billetStatutRepository.findAll().stream()
            .filter(s -> s.getLibelle().toLowerCase().contains("emis"))
            .findFirst()
            .orElse(billetStatutRepository.findAll().get(0));
        
        // Calculer le montant total
        BigDecimal montantTotal = BigDecimal.ZERO;
        
        // Créer les billets pour chaque siège avec son client et prix spécifique
        for (Map.Entry<Integer, Map<String, Object>> entry : siegesData.entrySet()) {
            Integer siegeId = entry.getKey();
            Map<String, Object> data = entry.getValue();
            
            Integer clientId = (Integer) data.get("clientId");
            BigDecimal prix = (BigDecimal) data.get("prix");
            
            AvionSiege siege = avionSiegeRepository.findById(siegeId)
                .orElseThrow(() -> new IllegalArgumentException("Siège non trouvé: " + siegeId));
            
            Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé: " + clientId));
            
            montantTotal = montantTotal.add(prix);
            
            ReservationBillet billet = new ReservationBillet();
            billet.setPrix(prix);
            billet.setAvionSiege(siege);
            billet.setReservation(reservation);
            billet.setBilletStatut(billetStatut);
            billet.setClient(client); // Associer le client spécifique à ce billet
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
