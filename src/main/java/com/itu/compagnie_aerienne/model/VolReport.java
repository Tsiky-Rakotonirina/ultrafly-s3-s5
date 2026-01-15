package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "vol_report")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolReport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vol_report_generator")
    @SequenceGenerator(name = "vol_report_generator", sequenceName = "seq_vol_report", allocationSize = 1)
    @Column(name = "id_vol_report")
    private Integer idVolReport;
    
    @Column(name = "numero", unique = true, length = 10)
    private String numero;
    
    @Column(name = "heure", nullable = false)
    private LocalDateTime heure;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vol_report_type_id", nullable = false)
    private VolReportType volReportType;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vol_detail_id", nullable = false)
    private VolDetail volDetail;
}
