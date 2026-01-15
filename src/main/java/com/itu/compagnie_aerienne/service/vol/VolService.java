package com.itu.compagnie_aerienne.service.vol;

import com.itu.compagnie_aerienne.model.*;
import com.itu.compagnie_aerienne.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VolService {

    @Autowired
    private VolRepository volRepository;
    
    @Autowired
    private VolDetailRepository volDetailRepository;
    
    @Autowired
    private VolTarrifRepository volTarrifRepository;
    
    @Autowired
    private VolHistoriqueRepository volHistoriqueRepository;
    
    @Autowired
    private VolReportRepository volReportRepository;
    
    @Autowired
    private VolArretRepository volArretRepository;
    
    @Autowired
    private VolStatutRepository volStatutRepository;
    
    @Autowired
    private VolTypeRepository volTypeRepository;
    
    @Autowired
    private VolReportTypeRepository volReportTypeRepository;
    
    @Autowired
    private ItineraireRepository itineraireRepository;
    
    @Autowired
    private ItineraireEscaleRepository itineraireEscaleRepository;
    
    @Autowired
    private AeroportRepository aeroportRepository;
    
    @Autowired
    private AvionRepository avionRepository;
    
    @Autowired
    private EquipageRepository equipageRepository;
    
    @Autowired
    private SiegeCategorieRepository siegeCategorieRepository;

    // ==================== CRUD Vol ====================
    
    public List<Vol> findAll() {
        return volRepository.findAll();
    }
    
    public Optional<Vol> findById(Integer id) {
        return volRepository.findById(id);
    }
    
    public Optional<Vol> findByNumero(String numero) {
        return volRepository.findByNumero(numero);
    }
    
    @Transactional
    public Vol save(Vol vol) {
        return volRepository.save(vol);
    }
    
    @Transactional
    public void delete(Integer id) {
        volRepository.deleteById(id);
    }

    // ==================== Filtres Vol ====================
    
    public List<Vol> filtrer(LocalDateTime heureDebut, LocalDateTime heureFin,
                             Integer volTypeId, Integer statutVolId,
                             Integer itineraireId, Integer aeroportDepartId,
                             Integer aeroportArriveeId) {
        return volRepository.filtrer(heureDebut, heureFin, volTypeId, statutVolId,
                itineraireId, aeroportDepartId, aeroportArriveeId);
    }

    // ==================== Détails Vol ====================
    
    public List<VolDetail> getDetailsByVol(Integer volId) {
        return volDetailRepository.findByVolIdVolOrderByHeureAsc(volId);
    }
    
    @Transactional
    public VolDetail saveDetail(VolDetail detail) {
        return volDetailRepository.save(detail);
    }

    // ==================== Tarifs Vol ====================
    
    public List<VolTarrif> getTarifsByVol(Integer volId) {
        return volTarrifRepository.findByVolIdVol(volId);
    }
    
    public Optional<VolTarrif> getTarifByVolAndCategorie(Integer volId, Integer siegeCategorieId) {
        return volTarrifRepository.findByVolIdVolAndSiegeCategorieIdSiegeCategorie(volId, siegeCategorieId);
    }
    
    @Transactional
    public VolTarrif saveTarif(VolTarrif tarif) {
        return volTarrifRepository.save(tarif);
    }

    // ==================== Historique / Statuts ====================
    
    public List<VolHistorique> getHistoriqueByVol(Integer volId) {
        return volHistoriqueRepository.findByVolIdVolOrderByDateStatutDesc(volId);
    }
    
    @Transactional
    public VolHistorique changerStatut(Integer volId, Integer statutId) {
        Vol vol = volRepository.findById(volId)
                .orElseThrow(() -> new RuntimeException("Vol non trouvé"));
        VolStatut statut = volStatutRepository.findById(statutId)
                .orElseThrow(() -> new RuntimeException("Statut non trouvé"));
        
        // Mettre à jour le statut du vol
        vol.setStatutVol(statut);
        volRepository.save(vol);
        
        // Créer l'historique
        VolHistorique historique = new VolHistorique();
        historique.setVol(vol);
        historique.setStatutVol(statut);
        historique.setDateStatut(LocalDate.now());
        
        return volHistoriqueRepository.save(historique);
    }

    // ==================== Report de Vol ====================
    
    public List<VolReport> getReportsByVol(Integer volId) {
        List<VolDetail> details = volDetailRepository.findByVolIdVolOrderByHeureAsc(volId);
        if (details.isEmpty()) return List.of();
        return volReportRepository.findByVolDetailIdVolDetailOrderByHeureDesc(details.get(0).getIdVolDetail());
    }
    
    @Transactional
    public VolReport reporterVol(Integer volDetailId, Integer reportTypeId, LocalDateTime nouvelleHeure) {
        VolDetail detail = volDetailRepository.findById(volDetailId)
                .orElseThrow(() -> new RuntimeException("Détail vol non trouvé"));
        VolReportType reportType = volReportTypeRepository.findById(reportTypeId)
                .orElseThrow(() -> new RuntimeException("Type de report non trouvé"));
        
        VolReport report = new VolReport();
        report.setVolDetail(detail);
        report.setVolReportType(reportType);
        report.setHeure(nouvelleHeure);
        
        // Mettre le vol en statut "Reporté"
        VolStatut statutReporte = volStatutRepository.findByLibelle("Reporte")
                .orElse(null);
        if (statutReporte != null) {
            changerStatut(detail.getVol().getIdVol(), statutReporte.getIdVolStatut());
        }
        
        return volReportRepository.save(report);
    }

    // ==================== Arrêt de Vol ====================
    
    public List<VolArret> getArretsByVol(Integer volId) {
        return volArretRepository.findByVolIdVolOrderByHeureAsc(volId);
    }
    
    @Transactional
    public VolArret arreterVol(Integer volId, Integer aeroportId, LocalDateTime heure) {
        Vol vol = volRepository.findById(volId)
                .orElseThrow(() -> new RuntimeException("Vol non trouvé"));
        Aeroport aeroport = aeroportRepository.findById(aeroportId)
                .orElseThrow(() -> new RuntimeException("Aéroport non trouvé"));
        
        VolArret arret = new VolArret();
        arret.setVol(vol);
        arret.setAeroport(aeroport);
        arret.setHeure(heure);
        
        return volArretRepository.save(arret);
    }
    
    @Transactional
    public void annulerVol(Integer volId) {
        VolStatut statutAnnule = volStatutRepository.findByLibelle("Annule")
                .orElseThrow(() -> new RuntimeException("Statut 'Annule' non trouvé"));
        changerStatut(volId, statutAnnule.getIdVolStatut());
    }

    // ==================== Données de référence ====================
    
    public List<VolType> getAllVolTypes() {
        return volTypeRepository.findAll();
    }
    
    public List<VolStatut> getAllStatuts() {
        return volStatutRepository.findAll();
    }
    
    public List<VolReportType> getAllReportTypes() {
        return volReportTypeRepository.findAll();
    }
    
    public List<Itineraire> getAllItineraires() {
        return itineraireRepository.findAll();
    }
    
    public List<Aeroport> getAllAeroports() {
        return aeroportRepository.findAll();
    }
    
    public List<Avion> getAllAvions() {
        return avionRepository.findAll();
    }
    
    public List<Equipage> getAllEquipages() {
        return equipageRepository.findAll();
    }
    
    public List<SiegeCategorie> getAllSiegeCategories() {
        return siegeCategorieRepository.findAll();
    }
    
    public Optional<Itineraire> getItineraireById(Integer id) {
        return itineraireRepository.findById(id);
    }
    
    public List<ItineraireEscale> getEscalesByItineraire(Integer itineraireId) {
        return itineraireEscaleRepository.findByItineraireIdItineraireOrderByIdItineraireEscaleAsc(itineraireId);
    }
    
    // ==================== BILLETS ====================
    
    public List<ReservationBillet> getAllBillets() {
        return volDetailRepository.findAll().stream()
            .flatMap(d -> reservationBilletRepository.findByReservationVolIdVol(d.getVol().getIdVol()).stream())
            .distinct()
            .toList();
    }
    
    @Autowired
    private ReservationBilletRepository reservationBilletRepository;
}
