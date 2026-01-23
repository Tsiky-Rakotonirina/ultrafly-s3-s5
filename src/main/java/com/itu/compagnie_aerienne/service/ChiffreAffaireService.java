package com.itu.compagnie_aerienne.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itu.compagnie_aerienne.model.Paiement;
import com.itu.compagnie_aerienne.model.PaiementDetail;
import com.itu.compagnie_aerienne.model.PubliciteDiffusionVol;
import com.itu.compagnie_aerienne.model.PubliciteTarrif;
import com.itu.compagnie_aerienne.model.Reservation;
import com.itu.compagnie_aerienne.model.Vol;
import com.itu.compagnie_aerienne.model.VolDetail;
import com.itu.compagnie_aerienne.repository.PaiementDetailRepository;
import com.itu.compagnie_aerienne.repository.PaiementRepository;
import com.itu.compagnie_aerienne.repository.PubliciteDiffusionVolRepository;
import com.itu.compagnie_aerienne.repository.PubliciteTarrifRepository;
import com.itu.compagnie_aerienne.repository.ReservationRepository;
import com.itu.compagnie_aerienne.repository.VolDetailRepository;
import com.itu.compagnie_aerienne.repository.VolRepository;

@Service
public class ChiffreAffaireService {
    
    private final VolRepository volRepository;
    private final VolDetailRepository volDetailRepository;
    private final ReservationRepository reservationRepository;
    private final PaiementRepository paiementRepository;
    private final PaiementDetailRepository paiementDetailRepository;
    private final PubliciteDiffusionVolRepository publiciteDiffusionVolRepository;
    private final PubliciteTarrifRepository publiciteTarrifRepository;

    public ChiffreAffaireService(
            VolRepository volRepository,
            VolDetailRepository volDetailRepository,
            ReservationRepository reservationRepository,
            PaiementRepository paiementRepository,
            PaiementDetailRepository paiementDetailRepository,
            PubliciteDiffusionVolRepository publiciteDiffusionVolRepository,
            PubliciteTarrifRepository publiciteTarrifRepository) {
        this.volRepository = volRepository;
        this.volDetailRepository = volDetailRepository;
        this.reservationRepository = reservationRepository;
        this.paiementRepository = paiementRepository;
        this.paiementDetailRepository = paiementDetailRepository;
        this.publiciteDiffusionVolRepository = publiciteDiffusionVolRepository;
        this.publiciteTarrifRepository = publiciteTarrifRepository;
    }

    /**
     * Récupère tous les vols
     */
    public List<Vol> getAllVols() {
        return volRepository.findAll();
    }

    /**
     * Calcule le montant total généré par la vente de tickets pour un vol
     */
    public BigDecimal getMontantTicketsParVol(Integer volId) {
        BigDecimal montantTotal = BigDecimal.ZERO;
        
        // Récupérer toutes les réservations pour ce vol
        List<Reservation> reservations = reservationRepository.findAllByVolIdVol(volId);
        
        for (Reservation reservation : reservations) {
            // Récupérer le paiement associé à la réservation
            var paiementOpt = paiementRepository.findByReservationIdReservation(reservation.getIdReservation());
            
            if (paiementOpt.isPresent()) {
                Paiement paiement = paiementOpt.get();
                // Récupérer les détails de paiement
                List<PaiementDetail> paiementDetails = paiementDetailRepository.findByPaiementIdPaiement(paiement.getIdPaiement());
                
                for (PaiementDetail detail : paiementDetails) {
                    montantTotal = montantTotal.add(detail.getMontant());
                }
            }
        }
        
        return montantTotal;
    }

    /**
     * Calcule le montant généré par la diffusion de publicité pour un vol
     */
    public BigDecimal getMontantPubliciteParVol(Integer volId) {
        BigDecimal montantTotal = BigDecimal.ZERO;
        
        // Récupérer toutes les diffusions de publicité pour ce vol
        List<PubliciteDiffusionVol> diffusions = publiciteDiffusionVolRepository.findByVolIdVol(volId);
        
        for (PubliciteDiffusionVol diffusion : diffusions) {
            if (diffusion.getPubliciteDiffusion() != null) {
                // Calculer le montant basé sur le nombre de diffusions et la durée
                BigDecimal duree = diffusion.getPubliciteDiffusion().getDuree();
                Integer nombre = diffusion.getNombre();
                
                // Récupérer le tarif applicable (le plus récent avant la date du vol)
                // Pour simplifier, on prend le dernier tarif disponible
                List<PubliciteTarrif> tarrifs = publiciteTarrifRepository.findAll();
                if (!tarrifs.isEmpty()) {
                    PubliciteTarrif tarrif = tarrifs.get(tarrifs.size() - 1);
                    
                    if (duree != null && tarrif.getCout() != null) {
                        BigDecimal montantParDiffusion = tarrif.getCout().multiply(duree);
                        montantTotal = montantTotal.add(montantParDiffusion.multiply(new BigDecimal(nombre)));
                    }
                }
            }
        }
        
        return montantTotal;
    }

    /**
     * Calcule le chiffre d'affaires total pour un vol (tickets + publicité)
     */
    public BigDecimal getChiffreAffaireTotalParVol(Integer volId) {
        BigDecimal montantTickets = getMontantTicketsParVol(volId);
        BigDecimal montantPublicite = getMontantPubliciteParVol(volId);
        return montantTickets.add(montantPublicite);
    }

    /**
     * Récupère les détails du vol pour obtenir l'avion
     */
    public List<VolDetail> getVolDetailsByVolId(Integer volId) {
        return volDetailRepository.findByVolIdVol(volId);
    }

    /**
     * Crée un objet Map avec toutes les données de chiffre d'affaires pour un vol
     */
    public Map<String, Object> getChiffreAffaireDataParVol(Vol vol) {
        Map<String, Object> data = new HashMap<>();
        
        // Informations de base du vol
        data.put("numeroVol", vol.getNumero());
        data.put("aeroportDepart", vol.getItineraire().getAeroportDepart().getNom());
        data.put("aeroportArrivee", vol.getItineraire().getAeroportArrive().getNom());
        data.put("dateDepart", vol.getHeure());
        
        // Récupérer l'avion
        List<VolDetail> volDetails = getVolDetailsByVolId(vol.getIdVol());
        String avionNom = "N/A";
        if (!volDetails.isEmpty() && volDetails.get(0).getAvion() != null) {
            avionNom = volDetails.get(0).getAvion().getModele();
        }
        data.put("avion", avionNom);
        
        // Calculs des montants
        BigDecimal montantTickets = getMontantTicketsParVol(vol.getIdVol());
        BigDecimal montantPublicite = getMontantPubliciteParVol(vol.getIdVol());
        BigDecimal montantTotal = montantTickets.add(montantPublicite);
        
        data.put("montantTickets", montantTickets);
        data.put("montantPublicite", montantPublicite);
        data.put("montantTotal", montantTotal);
        
        return data;
    }

    /**
     * Récupère toutes les données de chiffre d'affaires pour tous les vols
     */
    public List<Map<String, Object>> getAllChiffreAffaireData() {
        List<Vol> vols = getAllVols();
        List<Map<String, Object>> chiffresAffaires = new ArrayList<>();
        
        for (Vol vol : vols) {
            Map<String, Object> data = getChiffreAffaireDataParVol(vol);
            chiffresAffaires.add(data);
        }
        
        return chiffresAffaires;
    }

    /**
     * Calcule les totaux généraux
     */
    public Map<String, BigDecimal> getTotauxGeneraux() {
        Map<String, BigDecimal> totaux = new HashMap<>();
        BigDecimal totalTickets = BigDecimal.ZERO;
        BigDecimal totalPublicite = BigDecimal.ZERO;
        
        List<Vol> vols = getAllVols();
        for (Vol vol : vols) {
            totalTickets = totalTickets.add(getMontantTicketsParVol(vol.getIdVol()));
            totalPublicite = totalPublicite.add(getMontantPubliciteParVol(vol.getIdVol()));
        }
        
        totaux.put("totalTickets", totalTickets);
        totaux.put("totalPublicite", totalPublicite);
        totaux.put("totalGeneral", totalTickets.add(totalPublicite));
        
        return totaux;
    }
}
