package com.back.agricultorservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Table(name="Transportista")
public class TransportistaModel {

    @Id
    @Column(name = "CuiTransportista", nullable = false)
    private String cuiTransportista;

    @ManyToOne
    @JoinColumn(name = "NitAgricultor")
    private TblAgricultorModel agricultor;

    @Column(name = "NombreCompleto", nullable = false)
    private String NombreCompleto;

    @Column(name = "FechaNacimiento", nullable = false)
    private LocalDate FechaNacimiento;

    @Column(name = "TipoLicencia", nullable = true)
    private String TipoLicencia;

    @Column(name = "FechaVencimientoLicencia", nullable = true)
    private LocalDate FechaVencimientoLicencia;

    @Column(name = "Activo", nullable = false)
    private boolean activo = true;

    @Column(name = "Observaciones", nullable = true)
    private String Observaciones;

    @Column(name = "FechaCreacion", nullable = true)
    private LocalDateTime FechaCreacion;

    @Column(name = "UsuarioCreacion", nullable = true)
    private String UsuarioCreacion;

    @Column(name = "FechaModificacion", nullable = true)
    private LocalDateTime FechaModificacion;

    @Column(name = "UsuarioModificacion", nullable = true)
    private String UsuarioModificacion;
}
