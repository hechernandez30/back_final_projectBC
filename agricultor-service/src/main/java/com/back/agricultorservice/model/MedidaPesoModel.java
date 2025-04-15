package com.back.agricultorservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "MedidaPeso")
public class MedidaPesoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MedidaPesoID")
    private Long id;

    @Column(name = "NombrePeso", nullable = false)
    private String NombrePeso;

    @Column(name = "AbreviaturaPeso", nullable = false)
    private String abreviaturaPeso;

    @Column(name = "Activo", nullable = false)
    private boolean activo = true;

    @Column(name = "FechaCreacion", nullable = true)
    private LocalDateTime FechaCreacion;

    @Column(name = "UsuarioCreacion", nullable = true)
    private String UsuarioCreacion;

    @Column(name = "FechaModificacion", nullable = true)
    private LocalDateTime FechaModificacion;

    @Column(name = "UsuarioModificacion", nullable = true)
    private String UsuarioModificacion;
}
