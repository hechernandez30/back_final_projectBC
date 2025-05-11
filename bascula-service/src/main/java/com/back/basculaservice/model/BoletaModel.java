package com.back.basculaservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "boleta")
public class BoletaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="BoletaID")
    private int boletaId;
    @Column(name = "ParcialidadID")
    private int parcialidadId;
    private float monto;
    private String observaciones;
    private boolean activo = true;
    private LocalDate FechaCreacion;
    private String UsuarioCreacion;
    private LocalDate FechaModificacion;
    private String UsuarioModificacion;
}
