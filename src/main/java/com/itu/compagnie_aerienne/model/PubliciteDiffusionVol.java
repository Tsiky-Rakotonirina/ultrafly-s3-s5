package com.itu.compagnie_aerienne.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "publicite_diffusion_vol")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PubliciteDiffusionVol {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_publicite_diffusion_vol")
    private Integer idPubliciteDiffusionVol;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vol_id")
    private Vol vol;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publicite_diffusion_id")
    private PubliciteDiffusion publiciteDiffusion;
    
    @Column(name = "nombre", nullable = false)
    private Integer nombre;
}
