package com.itu.compagnie_aerienne.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itu.compagnie_aerienne.model.PubliciteDiffusion;
import com.itu.compagnie_aerienne.model.PubliciteTarrif;
import com.itu.compagnie_aerienne.model.Societe;
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
        private List<DiffusionParSociete> detailsParSociete;

        public DiffusionStats() {
            this.detailsParSociete = new ArrayList<>();
        }

        // Getters et Setters
        public BigDecimal getChiffreAffaireTotal() { return chiffreAffaireTotal; }
        public void setChiffreAffaireTotal(BigDecimal chiffreAffaireTotal) { this.chiffreAffaireTotal = chiffreAffaireTotal; }
        public Long getNombreDiffusionTotal() { return nombreDiffusionTotal; }
        public void setNombreDiffusionTotal(Long nombreDiffusionTotal) { this.nombreDiffusionTotal = nombreDiffusionTotal; }
        public List<DiffusionParSociete> getDetailsParSociete() { return detailsParSociete; }
        public void setDetailsParSociete(List<DiffusionParSociete> detailsParSociete) { this.detailsParSociete = detailsParSociete; }
    }

    /**
     * Classe pour stocker les détails par société
     */
    public static class DiffusionParSociete {
        private String nomSociete;
        private Long nombreDiffusion;
        private BigDecimal chiffreAffaire;

        public DiffusionParSociete(String nomSociete, Long nombreDiffusion, BigDecimal chiffreAffaire) {
            this.nomSociete = nomSociete;
            this.nombreDiffusion = nombreDiffusion;
            this.chiffreAffaire = chiffreAffaire;
        }

        // Getters
        public String getNomSociete() { return nomSociete; }
        public Long getNombreDiffusion() { return nombreDiffusion; }
        public BigDecimal getChiffreAffaire() { return chiffreAffaire; }
    }

    /**
     * Calcule le chiffre d'affaires et le nombre de diffusions
     * @param moisAnnee Date du mois-année (format YYYY-MM-01). Si null, calcule pour tous les mois
     * @return DiffusionStats contenant le CA total, nombre total et détails par société
     */
    public DiffusionStats calculerStatsDiffusion(LocalDate moisAnnee) {
        DiffusionStats stats = new DiffusionStats();
        
        // Calcul des totaux généraux
        StringBuilder queryTotal = new StringBuilder();
        queryTotal.append("SELECT COALESCE(SUM(pd.nombre * pt.cout), 0) as ca, COALESCE(SUM(pd.nombre), 0) as nb ");
        queryTotal.append("FROM publicite_diffusion pd ");
        queryTotal.append("LEFT JOIN LATERAL ( ");
        queryTotal.append("  SELECT pt.cout ");
        queryTotal.append("  FROM publicite_tarrif pt ");
        queryTotal.append("  WHERE pt.date_tarrif <= pd.mois_annee ");
        queryTotal.append("  ORDER BY pt.date_tarrif DESC ");
        queryTotal.append("  LIMIT 1 ");
        queryTotal.append(") pt ON true ");
        queryTotal.append("WHERE 1=1 ");
        
        if (moisAnnee != null) {
            queryTotal.append("AND pd.mois_annee = :moisAnnee ");
        }
        
        var nativeQueryTotal = entityManager.createNativeQuery(queryTotal.toString());
        if (moisAnnee != null) {
            nativeQueryTotal.setParameter("moisAnnee", moisAnnee);
        }
        
        Object[] resultTotal = (Object[]) nativeQueryTotal.getSingleResult();
        stats.setChiffreAffaireTotal(resultTotal[0] != null ? new BigDecimal(resultTotal[0].toString()) : BigDecimal.ZERO);
        stats.setNombreDiffusionTotal(resultTotal[1] != null ? Long.parseLong(resultTotal[1].toString()) : 0L);
        
        // Calcul des détails par société
        StringBuilder queryDetail = new StringBuilder();
        queryDetail.append("SELECT s.nom, COALESCE(SUM(pd.nombre), 0) as nb, COALESCE(SUM(pd.nombre * pt.cout), 0) as ca ");
        queryDetail.append("FROM publicite_diffusion pd ");
        queryDetail.append("JOIN societe s ON pd.societe_id = s.id_societe ");
        queryDetail.append("LEFT JOIN LATERAL ( ");
        queryDetail.append("  SELECT pt.cout ");
        queryDetail.append("  FROM publicite_tarrif pt ");
        queryDetail.append("  WHERE pt.date_tarrif <= pd.mois_annee ");
        queryDetail.append("  ORDER BY pt.date_tarrif DESC ");
        queryDetail.append("  LIMIT 1 ");
        queryDetail.append(") pt ON true ");
        queryDetail.append("WHERE 1=1 ");
        
        if (moisAnnee != null) {
            queryDetail.append("AND pd.mois_annee = :moisAnnee ");
        }
        
        queryDetail.append("GROUP BY s.id_societe, s.nom ");
        queryDetail.append("ORDER BY s.nom ");
        
        var nativeQueryDetail = entityManager.createNativeQuery(queryDetail.toString());
        if (moisAnnee != null) {
            nativeQueryDetail.setParameter("moisAnnee", moisAnnee);
        }
        
        @SuppressWarnings("unchecked")
        List<Object[]> resultsDetail = nativeQueryDetail.getResultList();
        List<DiffusionParSociete> detailsParSociete = new ArrayList<>();
        for (Object[] row : resultsDetail) {
            String nomSociete = (String) row[0];
            Long nombreDiffusion = row[1] != null ? Long.parseLong(row[1].toString()) : 0L;
            BigDecimal chiffreAffaire = row[2] != null ? new BigDecimal(row[2].toString()) : BigDecimal.ZERO;
            detailsParSociete.add(new DiffusionParSociete(nomSociete, nombreDiffusion, chiffreAffaire));
        }
        stats.setDetailsParSociete(detailsParSociete);
        
        return stats;
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



}
