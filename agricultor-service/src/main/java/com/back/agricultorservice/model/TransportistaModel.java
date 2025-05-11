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

    @Column(name = "NitAgricultor")
    private String nitAgricultor;

    @Column(name = "NombreCompleto", nullable = false)
    private String nombreCompleto;

    @Column(name = "FechaNacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "TipoLicencia", nullable = true)
    private String tipoLicencia;

    @Column(name = "FechaVencimientoLicencia", nullable = true)
    private LocalDate fechaVencimientoLicencia;

    @Column(name = "Activo", nullable = false)
    private boolean activo = true;

    @Column(name = "Observaciones", nullable = true)
    private String observaciones;

    @Column(name = "FechaCreacion", nullable = true)
    private LocalDateTime fechaCreacion;

    @Column(name = "UsuarioCreacion", nullable = true)
    private String usuarioCreacion;

    @Column(name = "FechaModificacion", nullable = true)
    private LocalDateTime fechaModificacion;

    @Column(name = "UsuarioModificacion", nullable = true)
    private String usuarioModificacion;
}
