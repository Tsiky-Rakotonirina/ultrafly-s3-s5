package com.itu.compagnie_aerienne.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itu.compagnie_aerienne.model.PubliciteDiffusion;
import com.itu.compagnie_aerienne.model.PubliciteTarrif;
import com.itu.compagnie_aerienne.model.Societe;
import com.itu.compagnie_aerienne.repository.EncaissementRepository;
import com.itu.compagnie_aerienne.repository.PubliciteDiffusionRepository;
import com.itu.compagnie_aerienne.repository.PubliciteTarrifRepository;
import com.itu.compagnie_aerienne.repository.PubliciteTypeRepository;
import com.itu.compagnie_aerienne.repository.SocieteRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiffusionService {
    private final SocieteRepository societeRepository;
    private final PubliciteDiffusionRepository publiciteDiffusionRepository;
    private final PubliciteTarrifRepository publiciteTarrifRepository;
    private final PubliciteTypeRepository publiciteTypeRepository;
    private final EncaissementRepository encaissementRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Societe> getAllSocietes() {
        return societeRepository.findAll();
    }

    public List<PubliciteTarrif> getAllPubliciteTarrifs() {
        return publiciteTarrifRepository.findAll();
    }

    /**
     * Classe pour stocker les résultats du calcul de CA
     */
    public static class DiffusionStats {
        private BigDecimal chiffreAffaireTotal;
        private Long nombreDiffusionTotal;
        private BigDecimal totalEncaisse;
        private BigDecimal resteAPayerTotal;
        private List<DiffusionParSociete> detailsParSociete;

        public DiffusionStats() {
            this.detailsParSociete = new ArrayList<>();
        }

        // Getters et Setters
        public BigDecimal getChiffreAffaireTotal() { return chiffreAffaireTotal; }
        public void setChiffreAffaireTotal(BigDecimal chiffreAffaireTotal) { this.chiffreAffaireTotal = chiffreAffaireTotal; }
        public Long getNombreDiffusionTotal() { return nombreDiffusionTotal; }
        public void setNombreDiffusionTotal(Long nombreDiffusionTotal) { this.nombreDiffusionTotal = nombreDiffusionTotal; }
        public BigDecimal getTotalEncaisse() { return totalEncaisse; }
        public void setTotalEncaisse(BigDecimal totalEncaisse) { this.totalEncaisse = totalEncaisse; }
        public BigDecimal getResteAPayerTotal() { return resteAPayerTotal; }
        public void setResteAPayerTotal(BigDecimal resteAPayerTotal) { this.resteAPayerTotal = resteAPayerTotal; }
        public List<DiffusionParSociete> getDetailsParSociete() { return detailsParSociete; }
        public void setDetailsParSociete(List<DiffusionParSociete> detailsParSociete) { this.detailsParSociete = detailsParSociete; }
    }

    /**
     * Classe pour stocker les détails par société
     */
    public static class DiffusionParSociete {
        private Integer idSociete;
        private String nomSociete;
        private Long nombreDiffusion;
        private BigDecimal chiffreAffaire;
        private BigDecimal montantEncaisse;
        private BigDecimal resteAPayer;

        public DiffusionParSociete(Integer idSociete, String nomSociete, Long nombreDiffusion, BigDecimal chiffreAffaire, BigDecimal montantEncaisse, BigDecimal resteAPayer) {
            this.idSociete = idSociete;
            this.nomSociete = nomSociete;
            this.nombreDiffusion = nombreDiffusion;
            this.chiffreAffaire = chiffreAffaire;
            this.montantEncaisse = montantEncaisse;
            this.resteAPayer = resteAPayer;
        }

        // Getters
        public Integer getIdSociete() { return idSociete; }
        public String getNomSociete() { return nomSociete; }
        public Long getNombreDiffusion() { return nombreDiffusion; }
        public BigDecimal getChiffreAffaire() { return chiffreAffaire; }
        public BigDecimal getMontantEncaisse() { return montantEncaisse; }
        public BigDecimal getResteAPayer() { return resteAPayer; }
    }

    /**
     * Calcule le chiffre d'affaires et le nombre de diffusions
     * @param dateFiltre Date de référence. Si fournie, calcule le CA du mois complet (début à fin du mois) et le reste à payer jusqu'à cette date. Si null, calcule pour toutes les périodes
     * @return DiffusionStats contenant le CA total, nombre total et détails par société
     */
    public DiffusionStats calculerStatsDiffusion(LocalDate dateFiltre) {
        DiffusionStats stats = new DiffusionStats();
        
        // Déterminer les dates de début et fin du mois si une date est fournie
        LocalDate dateDebutMois = null;
        LocalDate dateFinMois = null;
        if (dateFiltre != null) {
            dateDebutMois = dateFiltre.withDayOfMonth(1);
            dateFinMois = dateFiltre.withDayOfMonth(dateFiltre.lengthOfMonth());
        }
        
        // Calcul des totaux généraux (CA et nombre de diffusions)
        Object[] resultatsGlobaux = calculerTotauxGlobaux(dateDebutMois, dateFinMois);
        stats.setChiffreAffaireTotal(resultatsGlobaux[0] != null ? new BigDecimal(resultatsGlobaux[0].toString()) : BigDecimal.ZERO);
        stats.setNombreDiffusionTotal(resultatsGlobaux[1] != null ? Long.parseLong(resultatsGlobaux[1].toString()) : 0L);
        
        // Calcul des détails par société
        List<Object[]> resultsDetail = calculerDetailsParSociete(dateDebutMois, dateFinMois);
        
        // Traitement des résultats par société
        List<DiffusionParSociete> detailsParSociete = new ArrayList<>();
        BigDecimal totalEncaisse = BigDecimal.ZERO;
        BigDecimal resteAPayerTotal = BigDecimal.ZERO;
        
        for (Object[] row : resultsDetail) {
            DiffusionParSociete detail = traiterDetailSociete(row, dateFiltre);
            detailsParSociete.add(detail);
            totalEncaisse = totalEncaisse.add(detail.getMontantEncaisse());
            resteAPayerTotal = resteAPayerTotal.add(detail.getResteAPayer());
        }
        
        stats.setDetailsParSociete(detailsParSociete);
        stats.setTotalEncaisse(totalEncaisse);
        stats.setResteAPayerTotal(resteAPayerTotal);
        
        return stats;
    }
    
    /**
     * Calcule les totaux globaux (CA et nombre de diffusions)
     * @param dateDebut Date de début de la période (null = depuis toujours)
     * @param dateFin Date de fin de la période (null = jusqu'à maintenant)
     * @return Tableau contenant [chiffre d'affaires, nombre de diffusions]
     */
    private Object[] calculerTotauxGlobaux(LocalDate dateDebut, LocalDate dateFin) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT COALESCE(SUM(pd.nombre * pt.cout), 0) as ca, COALESCE(SUM(pd.nombre), 0) as nb ");
        query.append("FROM publicite_diffusion pd ");
        query.append("LEFT JOIN LATERAL ( ");
        query.append("  SELECT pt.cout ");
        query.append("  FROM publicite_tarrif pt ");
        query.append("  WHERE pt.date_tarrif <= pd.mois_annee ");
        query.append("  ORDER BY pt.date_tarrif DESC ");
        query.append("  LIMIT 1 ");
        query.append(") pt ON true ");
        query.append("WHERE 1=1 ");
        
        if (dateDebut != null && dateFin != null) {
            query.append("AND pd.mois_annee >= :dateDebut AND pd.mois_annee <= :dateFin ");
        }
        
        var nativeQuery = entityManager.createNativeQuery(query.toString());
        if (dateDebut != null && dateFin != null) {
            nativeQuery.setParameter("dateDebut", dateDebut);
            nativeQuery.setParameter("dateFin", dateFin);
        }
        
        return (Object[]) nativeQuery.getSingleResult();
    }
    
    /**
     * Calcule les détails par société (CA et nombre de diffusions)
     * @param dateDebut Date de début de la période (null = depuis toujours)
     * @param dateFin Date de fin de la période (null = jusqu'à maintenant)
     * @return Liste des résultats par société
     */
    @SuppressWarnings("unchecked")
    private List<Object[]> calculerDetailsParSociete(LocalDate dateDebut, LocalDate dateFin) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT s.id_societe, s.nom, COALESCE(SUM(pd.nombre), 0) as nb, COALESCE(SUM(pd.nombre * pt.cout), 0) as ca ");
        query.append("FROM publicite_diffusion pd ");
        query.append("JOIN societe s ON pd.societe_id = s.id_societe ");
        query.append("LEFT JOIN LATERAL ( ");
        query.append("  SELECT pt.cout ");
        query.append("  FROM publicite_tarrif pt ");
        query.append("  WHERE pt.date_tarrif <= pd.mois_annee ");
        query.append("  ORDER BY pt.date_tarrif DESC ");
        query.append("  LIMIT 1 ");
        query.append(") pt ON true ");
        query.append("WHERE 1=1 ");
        
        if (dateDebut != null && dateFin != null) {
            query.append("AND pd.mois_annee >= :dateDebut AND pd.mois_annee <= :dateFin ");
        }
        
        query.append("GROUP BY s.id_societe, s.nom ");
        query.append("ORDER BY s.nom ");
        
        var nativeQuery = entityManager.createNativeQuery(query.toString());
        if (dateDebut != null && dateFin != null) {
            nativeQuery.setParameter("dateDebut", dateDebut);
            nativeQuery.setParameter("dateFin", dateFin);
        }
        
        return nativeQuery.getResultList();
    }
    
    /**
     * Traite les détails d'une société (calcul des encaissements et reste à payer)
     * @param row Ligne de résultat [id_societe, nom, nombre_diffusions, chiffre_affaire]
     * @param dateFiltre Date de référence pour le calcul des encaissements (null = tous)
     * @return Objet DiffusionParSociete contenant tous les détails
     */
    private DiffusionParSociete traiterDetailSociete(Object[] row, LocalDate dateFiltre) {
        Integer idSociete = row[0] != null ? Integer.parseInt(row[0].toString()) : null;
        String nomSociete = (String) row[1];
        Long nombreDiffusion = row[2] != null ? Long.parseLong(row[2].toString()) : 0L;
        BigDecimal chiffreAffaire = row[3] != null ? new BigDecimal(row[3].toString()) : BigDecimal.ZERO;
        
        // Calcul des encaissements (jusqu'à la date de filtre si spécifiée)
        BigDecimal montantEncaisse = calculerEncaissementSociete(idSociete, dateFiltre);
        
        // Calcul du reste à payer
        BigDecimal resteAPayer = calculerResteAPayer(chiffreAffaire, montantEncaisse);
        
        return new DiffusionParSociete(idSociete, nomSociete, nombreDiffusion, chiffreAffaire, montantEncaisse, resteAPayer);
    }
    
    /**
     * Calcule le montant total encaissé pour une société
     * @param idSociete ID de la société
     * @param dateFiltre Date limite (null = tous les encaissements)
     * @return Montant total encaissé
     */
    private BigDecimal calculerEncaissementSociete(Integer idSociete, LocalDate dateFiltre) {
        BigDecimal montantEncaisse;
        if (dateFiltre != null) {
            montantEncaisse = encaissementRepository.sumMontantBySocieteIdAndDateBefore(idSociete, dateFiltre);
        } else {
            montantEncaisse = encaissementRepository.sumMontantBySocieteId(idSociete);
        }
        return montantEncaisse != null ? montantEncaisse : BigDecimal.ZERO;
    }
    
    /**
     * Calcule le reste à payer (CA - Encaissements)
     * @param chiffreAffaire Chiffre d'affaires total
     * @param montantEncaisse Montant déjà encaissé
     * @return Reste à payer (minimum 0)
     */
    private BigDecimal calculerResteAPayer(BigDecimal chiffreAffaire, BigDecimal montantEncaisse) {
        BigDecimal resteAPayer = chiffreAffaire.subtract(montantEncaisse);
        return resteAPayer.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : resteAPayer;
    }

    /**
     * Calcule le chiffre d'affaires des publicités diffusées (méthode legacy)
     */
    public BigDecimal calculerChiffreAffaire(LocalDate moisAnnee, Integer idSociete) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT COALESCE(SUM(pd.nombre * pt.cout), 0) ");
        query.append("FROM publicite_diffusion pd ");
        query.append("LEFT JOIN LATERAL ( ");
        query.append("  SELECT pt.cout ");
        query.append("  FROM publicite_tarrif pt ");
        query.append("  WHERE pt.date_tarrif <= pd.mois_annee ");
        query.append("  ORDER BY pt.date_tarrif DESC ");
        query.append("  LIMIT 1 ");
        query.append(") pt ON true ");
        query.append("WHERE 1=1 ");

        if (moisAnnee != null) {
            query.append("AND pd.mois_annee = :moisAnnee ");
        }

        if (idSociete != null) {
            query.append("AND pd.societe_id = :idSociete ");
        }

        var nativeQuery = entityManager.createNativeQuery(query.toString());

        if (moisAnnee != null) {
            nativeQuery.setParameter("moisAnnee", moisAnnee);
        }

        if (idSociete != null) {
            nativeQuery.setParameter("idSociete", idSociete);
        }

        Object result = nativeQuery.getSingleResult();
        return result != null ? new BigDecimal(result.toString()) : BigDecimal.ZERO;
    }

    public String create(Integer nombre, LocalDate moisAnnee, BigDecimal duree, Integer societeId) {
        return publiciteDiffusionRepository.save(
                new PubliciteDiffusion(
                        null,
                        societeRepository.findById(societeId).orElseThrow(),
                        moisAnnee,
                        nombre,
                        duree))
                .toString();
    }

    /**
     * Crée un nouvel encaissement pour une société
     * @param societeId ID de la société
     * @param dateEncaissement Date de l'encaissement
     * @param montant Montant de l'encaissement
     * @return L'encaissement créé
     */
    public String createEncaissement(Integer societeId, LocalDate dateEncaissement, BigDecimal montant) {
        com.itu.compagnie_aerienne.model.Encaissement encaissement = new com.itu.compagnie_aerienne.model.Encaissement(
                null,
                societeRepository.findById(societeId).orElseThrow(),
                dateEncaissement,
                montant
        );
        return encaissementRepository.save(encaissement).toString();
    }

}
