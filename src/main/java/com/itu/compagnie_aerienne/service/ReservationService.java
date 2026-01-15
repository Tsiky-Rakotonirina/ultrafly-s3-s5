package com.itu.compagnie_aerienne.service;

import com.itu.compagnie_aerienne.model.*;
import com.itu.compagnie_aerienne.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservationService {
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private ReservationBilletRepository reservationBilletRepository;
    
    @Autowired
    private ReservationStatutRepository reservationStatutRepository;
    
    @Autowired
    private ReservationHistoriqueRepository reservationHistoriqueRepository;
    
    @Autowired
    private BilletStatutRepository billetStatutRepository;
    
    @Autowired
    private PaiementRepository paiementRepository;
    
    @Autowired
    private PaiementDetailRepository paiementDetailRepository;
    
    @Autowired
    private PaiementModeRepository paiementModeRepository;
    
    @Autowired
    private DeviseRepository deviseRepository;
    
    @Autowired
    private ChangeRepository changeRepository;
    
    @Autowired
    private VolRepository volRepository;
    
    @Autowired
    private VolTarrifRepository volTarrifRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private AvionSiegeRepository avionSiegeRepository;
    
    // ==================== RESERVATION ====================
    
    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }
    
    public Optional<Reservation> findById(Integer id) {
        return reservationRepository.findById(id);
    }
    
    public List<Reservation> filtrer(String numero, Integer clientId, Integer volId, Integer statutId, 
                                     LocalDate dateDebut, LocalDate dateFin) {
        return reservationRepository.filtrer(numero, clientId, volId, statutId, dateDebut, dateFin);
    }
    
    public Reservation creerReservation(Integer clientId, Integer volId, List<Integer> siegeIds) {
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        Vol vol = volRepository.findById(volId)
            .orElseThrow(() -> new RuntimeException("Vol non trouvé"));
        
        // Vérifier que les sièges sont disponibles
        for (Integer siegeId : siegeIds) {
            if (isSiegeReserve(volId, siegeId)) {
                AvionSiege siege = avionSiegeRepository.findById(siegeId).orElse(null);
                throw new RuntimeException("Le siège " + (siege != null ? siege.getNumero() : siegeId) + " est déjà réservé");
            }
        }
        
        // Statut initial: En attente
        ReservationStatut statut = reservationStatutRepository.findByLibelle("En attente")
            .orElseGet(() -> reservationStatutRepository.findAll().get(0));
        
        // Créer la réservation
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setVol(vol);
        reservation.setDateReservation(LocalDate.now());
        reservation.setReservationStatut(statut);
        
        reservation = reservationRepository.save(reservation);
        
        // Créer les billets
        BilletStatut billetStatut = billetStatutRepository.findByLibelle("Reserve")
            .orElseGet(() -> billetStatutRepository.findAll().get(0));
        
        BigDecimal montantTotal = BigDecimal.ZERO;
        
        for (Integer siegeId : siegeIds) {
            AvionSiege siege = avionSiegeRepository.findById(siegeId)
                .orElseThrow(() -> new RuntimeException("Siège non trouvé"));
            
            // Trouver le tarif pour cette catégorie de siège
            BigDecimal prix = getTarifPourSiege(vol, siege);
            
            ReservationBillet billet = new ReservationBillet();
            billet.setReservation(reservation);
            billet.setAvionSiege(siege);
            billet.setPrix(prix);
            billet.setBilletStatut(billetStatut);
            
            reservationBilletRepository.save(billet);
            montantTotal = montantTotal.add(prix);
        }
        
        // Créer le paiement associé
        Paiement paiement = new Paiement();
        paiement.setReservation(reservation);
        paiement.setMontantTotal(montantTotal);
        paiement.setRestePayer(montantTotal);
        paiementRepository.save(paiement);
        
        // Historique
        enregistrerHistorique(reservation);
        
        return reservation;
    }
    
    public boolean isSiegeReserve(Integer volId, Integer siegeId) {
        // Un siège est réservé s'il existe un billet pour ce siège sur ce vol
        // et que le billet n'est pas annulé
        return reservationBilletRepository.existsBySiegeAndVolNonAnnule(siegeId, volId);
    }
    
    public List<Integer> getSiegesReserves(Integer volId) {
        return reservationBilletRepository.findSiegesReservesByVol(volId);
    }
    
    private BigDecimal getTarifPourSiege(Vol vol, AvionSiege siege) {
        VolTarrif tarif = volTarrifRepository.findByVolIdVolAndSiegeCategorieIdSiegeCategorie(
            vol.getIdVol(), siege.getSiegeCategorie().getIdSiegeCategorie())
            .orElse(null);
        
        if (tarif != null) {
            return tarif.getPrix();
        }
        
        // Prix par défaut basé sur la catégorie
        return BigDecimal.valueOf(500000); // Prix par défaut
    }
    
    public void changerStatut(Integer reservationId, Integer statutId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        ReservationStatut statut = reservationStatutRepository.findById(statutId)
            .orElseThrow(() -> new RuntimeException("Statut non trouvé"));
        
        reservation.setReservationStatut(statut);
        reservationRepository.save(reservation);
        
        enregistrerHistorique(reservation);
    }
    
    public void annulerReservation(Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        
        ReservationStatut statutAnnule = reservationStatutRepository.findByLibelle("Annule")
            .orElseGet(() -> reservationStatutRepository.findAll().stream()
                .filter(s -> s.getLibelle().toLowerCase().contains("annul"))
                .findFirst().orElse(null));
        
        if (statutAnnule != null) {
            reservation.setReservationStatut(statutAnnule);
            reservationRepository.save(reservation);
            
            // Annuler aussi les billets
            BilletStatut billetAnnule = billetStatutRepository.findByLibelle("Annule")
                .orElse(null);
            if (billetAnnule != null) {
                List<ReservationBillet> billets = reservationBilletRepository.findByReservationIdReservation(reservationId);
                for (ReservationBillet billet : billets) {
                    billet.setBilletStatut(billetAnnule);
                    reservationBilletRepository.save(billet);
                }
            }
            
            enregistrerHistorique(reservation);
        }
    }
    
    private void enregistrerHistorique(Reservation reservation) {
        ReservationHistorique historique = new ReservationHistorique();
        historique.setReservation(reservation);
        historique.setReservationStatut(reservation.getReservationStatut());
        historique.setDateStatut(LocalDate.now());
        reservationHistoriqueRepository.save(historique);
    }
    
    // ==================== BILLETS ====================
    
    public List<ReservationBillet> findBilletsByReservation(Integer reservationId) {
        return reservationBilletRepository.findByReservationIdReservationOrderByIdReservationBilletAsc(reservationId);
    }
    
    public BigDecimal getMontantTotal(Integer reservationId) {
        List<ReservationBillet> billets = findBilletsByReservation(reservationId);
        return billets.stream()
            .map(ReservationBillet::getPrix)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // ==================== PAIEMENT ====================
    
    public Paiement findPaiementByReservation(Integer reservationId) {
        return paiementRepository.findByReservationIdReservation(reservationId).orElse(null);
    }
    
    public List<PaiementDetail> findPaiementDetailsByPaiement(Integer paiementId) {
        return paiementDetailRepository.findByPaiementIdPaiementOrderByDatePaiementDesc(paiementId);
    }
    
    public PaiementDetail effectuerPaiement(Integer reservationId, BigDecimal montant, Integer deviseId, Integer paiementModeId) {
        Paiement paiement = paiementRepository.findByReservationIdReservation(reservationId)
            .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
        
        Devise devise = deviseRepository.findById(deviseId)
            .orElseThrow(() -> new RuntimeException("Devise non trouvée"));
        PaiementMode mode = paiementModeRepository.findById(paiementModeId)
            .orElseThrow(() -> new RuntimeException("Mode de paiement non trouvé"));
        
        // Convertir le montant en MGA si nécessaire
        BigDecimal montantMGA = convertirEnMGA(montant, devise);
        
        if (montantMGA.compareTo(paiement.getRestePayer()) > 0) {
            throw new RuntimeException("Le montant dépasse le reste à payer");
        }
        
        // Créer le détail du paiement
        PaiementDetail detail = new PaiementDetail();
        detail.setPaiement(paiement);
        detail.setMontant(montant);
        detail.setDevise(devise);
        detail.setPaiementMode(mode);
        detail.setDatePaiement(LocalDate.now());
        
        paiementDetailRepository.save(detail);
        
        // Mettre à jour le reste à payer
        paiement.setRestePayer(paiement.getRestePayer().subtract(montantMGA));
        paiementRepository.save(paiement);
        
        // Mettre à jour le statut des billets si paiement complet
        if (paiement.getRestePayer().compareTo(BigDecimal.ZERO) <= 0) {
            marquerBilletsPayes(reservationId);
            
            // Changer le statut de la réservation à "Confirmé"
            ReservationStatut statutConfirme = reservationStatutRepository.findByLibelle("Confirme")
                .orElse(null);
            if (statutConfirme != null) {
                Reservation reservation = paiement.getReservation();
                reservation.setReservationStatut(statutConfirme);
                reservationRepository.save(reservation);
                enregistrerHistorique(reservation);
            }
        } else {
            // Marquer les billets payés par ordre d'id
            marquerBilletsPayesPartiellement(reservationId, montantMGA);
        }
        
        return detail;
    }
    
    private BigDecimal convertirEnMGA(BigDecimal montant, Devise devise) {
        if ("MGA".equals(devise.getCode())) {
            return montant;
        }
        
        // Trouver le taux de change le plus récent
        Change change = changeRepository.findTopByDeviseIdDeviseOrderByDateChangeDesc(devise.getIdDevise())
            .orElseThrow(() -> new RuntimeException("Taux de change non disponible pour " + devise.getCode()));
        
        return montant.multiply(change.getCours()).setScale(2, RoundingMode.HALF_UP);
    }
    
    private void marquerBilletsPayes(Integer reservationId) {
        BilletStatut statutPaye = billetStatutRepository.findByLibelle("Paye")
            .orElse(null);
        
        if (statutPaye != null) {
            List<ReservationBillet> billets = reservationBilletRepository.findByReservationIdReservationOrderByIdReservationBilletAsc(reservationId);
            for (ReservationBillet billet : billets) {
                billet.setBilletStatut(statutPaye);
                reservationBilletRepository.save(billet);
            }
        }
    }
    
    private void marquerBilletsPayesPartiellement(Integer reservationId, BigDecimal montantPaye) {
        // Marquer les billets comme payés par ordre d'id jusqu'à épuisement du montant
        BilletStatut statutPaye = billetStatutRepository.findByLibelle("Paye").orElse(null);
        if (statutPaye == null) return;
        
        List<ReservationBillet> billets = reservationBilletRepository.findByReservationIdReservationOrderByIdReservationBilletAsc(reservationId);
        BigDecimal cumul = BigDecimal.ZERO;
        
        // Calculer le total déjà payé
        Paiement paiement = paiementRepository.findByReservationIdReservation(reservationId).orElse(null);
        if (paiement == null) return;
        
        BigDecimal totalPaye = paiement.getMontantTotal().subtract(paiement.getRestePayer());
        
        for (ReservationBillet billet : billets) {
            cumul = cumul.add(billet.getPrix());
            if (cumul.compareTo(totalPaye) <= 0) {
                billet.setBilletStatut(statutPaye);
                reservationBilletRepository.save(billet);
            }
        }
    }
    
    public BigDecimal getRestePayer(Integer reservationId) {
        Paiement paiement = findPaiementByReservation(reservationId);
        return paiement != null ? paiement.getRestePayer() : BigDecimal.ZERO;
    }
    
    public BigDecimal getTotalPaye(Integer reservationId) {
        Paiement paiement = findPaiementByReservation(reservationId);
        if (paiement == null) return BigDecimal.ZERO;
        return paiement.getMontantTotal().subtract(paiement.getRestePayer());
    }
    
    // ==================== HISTORIQUE ====================
    
    public List<ReservationHistorique> findHistoriqueByReservation(Integer reservationId) {
        return reservationHistoriqueRepository.findByReservationIdReservationOrderByDateStatutDesc(reservationId);
    }
    
    // ==================== REFERENCES ====================
    
    public List<ReservationStatut> findAllStatuts() {
        return reservationStatutRepository.findAll();
    }
    
    public List<BilletStatut> findAllBilletStatuts() {
        return billetStatutRepository.findAll();
    }
    
    public List<Devise> findAllDevises() {
        return deviseRepository.findAll();
    }
    
    public List<PaiementMode> findAllPaiementModes() {
        return paiementModeRepository.findAll();
    }
    
    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }
    
    public List<Vol> findAllVols() {
        return volRepository.findAll();
    }
    
    public List<Vol> findVolsDisponibles() {
        // Vols programmés ou en cours
        return volRepository.findVolsDisponiblesPourReservation();
    }
    
    // ==================== SIÈGES POUR UN VOL ====================
    
    public List<AvionSiege> getSiegesPourVol(Integer volId) {
        Vol vol = volRepository.findById(volId)
            .orElseThrow(() -> new RuntimeException("Vol non trouvé"));
        
        // Ici il faudrait récupérer l'avion assigné au vol
        // Pour l'instant, on suppose qu'il y a un avion par défaut
        // TODO: Ajouter la relation Vol -> Avion
        
        return avionSiegeRepository.findAll(); // Placeholder
    }
    
    public List<SiegeInfo> getSiegesAvecDisponibilite(Integer volId, Integer avionId) {
        List<AvionSiege> sieges = avionSiegeRepository.findByAvionIdAvionOrderBySiegeCategorieIdSiegeCategorieAscRangeeAscColonneAsc(avionId);
        List<Integer> siegesReserves = getSiegesReserves(volId);
        
        // Récupérer les tarifs pour ce vol
        Vol vol = volRepository.findById(volId).orElse(null);
        
        List<SiegeInfo> result = new ArrayList<>();
        for (AvionSiege siege : sieges) {
            SiegeInfo info = new SiegeInfo();
            info.setSiege(siege);
            info.setReserve(siegesReserves.contains(siege.getIdAvionSiege()));
            info.setPrix(vol != null ? getTarifPourSiege(vol, siege) : BigDecimal.ZERO);
            result.add(info);
        }
        
        return result;
    }
    
    // Classe interne pour les informations de siège
    public static class SiegeInfo {
        private AvionSiege siege;
        private boolean reserve;
        private BigDecimal prix;
        
        public AvionSiege getSiege() { return siege; }
        public void setSiege(AvionSiege siege) { this.siege = siege; }
        public boolean isReserve() { return reserve; }
        public void setReserve(boolean reserve) { this.reserve = reserve; }
        public BigDecimal getPrix() { return prix; }
        public void setPrix(BigDecimal prix) { this.prix = prix; }
    }
}
