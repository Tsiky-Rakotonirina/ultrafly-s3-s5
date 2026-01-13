package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "change")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Change {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_change")
    private Integer idChange;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "devise_id", nullable = false)
    private Devise devise;
    
    @Column(name = "date_change", nullable = false)
    private LocalDate dateChange;
    
    @Column(name = "cours", nullable = false, precision = 10, scale = 4)
    private BigDecimal cours;
}
