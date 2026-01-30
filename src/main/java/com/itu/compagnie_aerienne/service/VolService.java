package com.itu.compagnie_aerienne.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.itu.compagnie_aerienne.model.AvionSiege;
import com.itu.compagnie_aerienne.model.Encaissement;
import com.itu.compagnie_aerienne.model.Paiement;
import com.itu.compagnie_aerienne.model.PaiementDetail;
import com.itu.compagnie_aerienne.model.PubliciteDiffusionVol;
import com.itu.compagnie_aerienne.model.Reservation;
import com.itu.compagnie_aerienne.model.ReservationBillet;
import com.itu.compagnie_aerienne.model.SiegeCategorie;
import com.itu.compagnie_aerienne.model.Vol;
import com.itu.compagnie_aerienne.model.VolDetail;
import com.itu.compagnie_aerienne.model.VolTarrif;
import com.itu.compagnie_aerienne.repository.AvionSiegeRepository;
import com.itu.compagnie_aerienne.repository.EncaissementRepository;
import com.itu.compagnie_aerienne.repository.PaiementDetailRepository;
import com.itu.compagnie_aerienne.repository.PaiementRepository;
import com.itu.compagnie_aerienne.repository.PubliciteDiffusionVolRepository;
import com.itu.compagnie_aerienne.repository.ReservationBilletRepository;
import com.itu.compagnie_aerienne.repository.ReservationRepository;
import com.itu.compagnie_aerienne.repository.VolDetailRepository;
import com.itu.compagnie_aerienne.repository.VolRepository;
import com.itu.compagnie_aerienne.repository.VolTarrifRepository;

@Service
public class VolService {
    private final VolRepository volRepository;
    private final VolDetailRepository volDetailRepository;
    private final AvionSiegeRepository avionSiegeRepository;
    private final VolTarrifRepository volTarrifRepository;
    private final ReservationRepository reservationRepository;
    private final PaiementRepository paiementRepository;
    private final PaiementDetailRepository paiementDetailRepository;
    private final ReservationBilletRepository reservationBilletRepository;
    private final PubliciteDiffusionVolRepository publiciteDiffusionVolRepository;
    private final EncaissementRepository encaissementRepository;

    public VolService(VolRepository volRepository, VolDetailRepository volDetailRepository, AvionSiegeRepository avionSiegeRepository, VolTarrifRepository volTarrifRepository, ReservationRepository reservationRepository, PaiementRepository paiementRepository, PaiementDetailRepository paiementDetailRepository, ReservationBilletRepository reservationBilletRepository, PubliciteDiffusionVolRepository publiciteDiffusionVolRepository, EncaissementRepository encaissementRepository) {
        this.volRepository = volRepository;
        this.volDetailRepository = volDetailRepository;
        this.avionSiegeRepository = avionSiegeRepository;
        this.volTarrifRepository = volTarrifRepository;
        this.reservationRepository = reservationRepository;
        this.paiementRepository = paiementRepository;
        this.paiementDetailRepository = paiementDetailRepository;
        this.reservationBilletRepository = reservationBilletRepository;
        this.publiciteDiffusionVolRepository = publiciteDiffusionVolRepository;
        this.encaissementRepository = encaissementRepository;
    }

    public List<Vol> getAllVol(){
        return volRepository.findAll();
    }

    public List<Vol> getVolsByDateRange(java.time.LocalDateTime dateDebut, java.time.LocalDateTime dateFin){
        return volRepository.findByHeureBetween(dateDebut, dateFin);
    }

    public List<VolDetail> getVolDetailsByVolId(Integer volId){
        return volDetailRepository.findByVolIdVol(volId);
    }

    public List<AvionSiege> getAvionSiegesByAvionId(Integer avionId){
        return avionSiegeRepository.findByAvionIdAvion(avionId);
    }

    public List<VolTarrif> getVolTarrifsByVolId(Integer volId){
        return volTarrifRepository.findAllByVolIdVol(volId);
    }

    public HashMap<SiegeCategorie, BigDecimal> getNbrAvionSiegeOrderBySiegeCategorie(Integer avionId){
        List<AvionSiege> listeAvionSiege = getAvionSiegesByAvionId(avionId);
        HashMap<SiegeCategorie, BigDecimal> nbrAvionSiegeParSiegeCategorie = new HashMap<>();

        for (AvionSiege avionSiege : listeAvionSiege){
            SiegeCategorie siegeCategorie = avionSiege.getSiegeCategorie();
            BigDecimal nbrSiege = nbrAvionSiegeParSiegeCategorie.getOrDefault(siegeCategorie, BigDecimal.ZERO);
            nbrAvionSiegeParSiegeCategorie.put(siegeCategorie, nbrSiege.add(BigDecimal.ONE));
        }

        return nbrAvionSiegeParSiegeCategorie;
    }

    public HashMap<SiegeCategorie, BigDecimal> getRecetteMaxBySiegeCategorie(Integer volId, Integer avionId){
        HashMap<SiegeCategorie, BigDecimal> nbrAvionSiegeParSiegeCategorie = getNbrAvionSiegeOrderBySiegeCategorie(avionId);
        HashMap<SiegeCategorie, BigDecimal> recetteMaxParSiegeCategorie = new HashMap<>();

        for (Map.Entry<SiegeCategorie, BigDecimal> entry : nbrAvionSiegeParSiegeCategorie.entrySet()) {
            SiegeCategorie siegeCategorie = entry.getKey();
            BigDecimal nbrSiege = entry.getValue();

            Optional<VolTarrif> volTarrifOpt = volTarrifRepository.findByVolAndCategorie(volId, siegeCategorie.getIdSiegeCategorie());
            
            // Si aucun tarif n'est défini pour cette catégorie, on met 0
            if (volTarrifOpt.isPresent()) {
                VolTarrif volTarrif = volTarrifOpt.get();
                BigDecimal tarrif = volTarrif.getPrix();
                BigDecimal recetteMax = tarrif.multiply(nbrSiege);
                recetteMaxParSiegeCategorie.put(siegeCategorie, recetteMax);
            } else {
                recetteMaxParSiegeCategorie.put(siegeCategorie, BigDecimal.ZERO);
            }
        }

        return recetteMaxParSiegeCategorie;
    }

    public HashMap<SiegeCategorie, BigDecimal> getTarrifBySiegeCategorie(Integer volId){
        List<VolTarrif> tarrifs = getVolTarrifsByVolId(volId);
        HashMap<SiegeCategorie, BigDecimal> tarrifParCategorie = new HashMap<>();

        for (VolTarrif tarrif : tarrifs) {
            if (tarrif != null && tarrif.getSiegeCategorie() != null && tarrif.getPrix() != null) {
                tarrifParCategorie.put(tarrif.getSiegeCategorie(), tarrif.getPrix());
            }
        }

        return tarrifParCategorie;
    }

    public BigDecimal chiffreAffaire(Integer volId){
        BigDecimal chiffreAffaire = BigDecimal.ZERO;

        // 1. Récupérer toutes les réservations pour ce vol
        List<Reservation> reservations = reservationRepository.findAllByVolIdVol(volId);
        
        // 2. Extraire les IDs des réservations
        List<Integer> reservationIds = new ArrayList<>();
        for (Reservation reservation : reservations) {
            reservationIds.add(reservation.getIdReservation());
        }
        
        // Si aucune réservation, retourner 0
        if (reservationIds.isEmpty()) {
            return chiffreAffaire;
        }
        
        // 3. Récupérer tous les paiements pour ces réservations
        List<Paiement> paiements = paiementRepository.findByReservationIdReservationIn(reservationIds);
        
        // 4. Extraire les IDs des paiements
        List<Integer> paiementIds = new ArrayList<>();
        for (Paiement paiement : paiements) {
            paiementIds.add(paiement.getIdPaiement());
        }
        
        // Si aucun paiement, retourner 0
        if (paiementIds.isEmpty()) {
            return chiffreAffaire;
        }
        
        // 5. Récupérer tous les détails de paiements
        List<PaiementDetail> paiementDetails = paiementDetailRepository.findByPaiementIdPaiementIn(paiementIds);
        
        // 6. Sommer les montants
        for (PaiementDetail detail : paiementDetails) {
            chiffreAffaire = chiffreAffaire.add(detail.getMontant());
        }

        return chiffreAffaire;
    }

    public HashMap<SiegeCategorie, BigDecimal> getSiegesPrisParCategorie(Integer volId){
        HashMap<SiegeCategorie, BigDecimal> siegesPrisParCategorie = new HashMap<>();

        // 1. Récupérer toutes les réservations pour ce vol
        List<Reservation> reservations = reservationRepository.findAllByVolIdVol(volId);
        
        // 2. Extraire les IDs des réservations
        List<Integer> reservationIds = new ArrayList<>();
        for (Reservation reservation : reservations) {
            reservationIds.add(reservation.getIdReservation());
        }
        
        // Si aucune réservation, retourner une HashMap vide
        if (reservationIds.isEmpty()) {
            return siegesPrisParCategorie;
        }
        
        // 3. Récupérer tous les billets pour ces réservations
        List<ReservationBillet> billets = reservationBilletRepository.findByReservationIdReservationIn(reservationIds);
        
        // 4. Compter les sièges par catégorie
        for (ReservationBillet billet : billets) {
            // Récupérer la catégorie du siège via AvionSiege
            SiegeCategorie categorie = billet.getAvionSiege().getSiegeCategorie();
            
            // Incrémenter le compteur pour cette catégorie
            BigDecimal count = siegesPrisParCategorie.getOrDefault(categorie, BigDecimal.ZERO);
            siegesPrisParCategorie.put(categorie, count.add(BigDecimal.ONE));
        }

        return siegesPrisParCategorie;
    }

    public List<Reservation> getAllByVolIdVol(Integer volId){
        return reservationRepository.findAllByVolIdVol(volId);
    }

    public Paiement getPaiementByReservationId(Integer reservationId){
        return paiementRepository.findByReservationIdReservation(reservationId).orElse(null);
    }

    /**
     * Calcule le chiffre d'affaires des publicités pour un vol donné
     * CA Publicité = Somme(Montant - Reste à payer) pour tous les encaissements liés aux publicités diffusées sur ce vol
     */
    public BigDecimal chiffreAffairePublicite(Integer volId) {
        BigDecimal chiffreAffairePublicite = BigDecimal.ZERO;
        
        // 1. Récupérer toutes les publicités diffusées pour ce vol
        List<PubliciteDiffusionVol> publiciteDiffusionVols = publiciteDiffusionVolRepository.findByVolIdVol(volId);
        
        // 2. Pour chaque publicité diffusée, récupérer les encaissements
        for (PubliciteDiffusionVol pdv : publiciteDiffusionVols) {
            Integer publiciteDiffusionVolId = pdv.getIdPubliciteDiffusionVol();
            
            // 3. Récupérer tous les encaissements pour cette publicité diffusion vol
            List<Encaissement> encaissements = encaissementRepository.findByPubliciteDiffusionVolIdPubliciteDiffusionVol(publiciteDiffusionVolId);
            
            for (Encaissement encaissement : encaissements) {
                // Calculer le montant encaissé = montant - reste à payer
                BigDecimal montantEncaisse = encaissement.getMontant().subtract(encaissement.getResteAPayer());
                chiffreAffairePublicite = chiffreAffairePublicite.add(montantEncaisse);
            }
        }
        
        return chiffreAffairePublicite;
    }
    
    /**
     * Calcule le montant total des publicités à payer pour un vol (toutes sociétés confondues)
     */
    public BigDecimal getMontantTotalPubliciteParVol(Integer volId) {
        BigDecimal montantTotal = BigDecimal.ZERO;
        
        List<PubliciteDiffusionVol> publiciteDiffusionVols = publiciteDiffusionVolRepository.findByVolIdVol(volId);
        
        for (PubliciteDiffusionVol pdv : publiciteDiffusionVols) {
            List<Encaissement> encaissements = encaissementRepository.findByPubliciteDiffusionVolIdPubliciteDiffusionVol(pdv.getIdPubliciteDiffusionVol());
            
            for (Encaissement encaissement : encaissements) {
                montantTotal = montantTotal.add(encaissement.getMontant());
            }
        }
        
        return montantTotal;
    }
    
    /**
     * Calcule le reste à payer des publicités pour un vol (toutes sociétés confondues)
     */
    public BigDecimal getResteAPayerPubliciteParVol(Integer volId) {
        BigDecimal resteAPayer = BigDecimal.ZERO;
        
        List<PubliciteDiffusionVol> publiciteDiffusionVols = publiciteDiffusionVolRepository.findByVolIdVol(volId);
        
        for (PubliciteDiffusionVol pdv : publiciteDiffusionVols) {
            List<Encaissement> encaissements = encaissementRepository.findByPubliciteDiffusionVolIdPubliciteDiffusionVol(pdv.getIdPubliciteDiffusionVol());
            
            for (Encaissement encaissement : encaissements) {
                resteAPayer = resteAPayer.add(encaissement.getResteAPayer());
            }
        }
        
        return resteAPayer;
    }
    
}
