package com.spring.security.jwt.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "historialestadocuenta")
public class HistorialEstadoCuentaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HistorialEstadoCuentaID")
    private Integer historialEstadoCuentaId;

    @ManyToOne
    @JoinColumn(name = "CuentaID", referencedColumnName = "CuentaID")
    private CuentaModel cuentaId;

    @ManyToOne
    @JoinColumn(name = "EstadoCuentaAnteriorID", referencedColumnName = "EstadoCuentaID")
    private EstadoCuentaModel estadoCuentaAnterior;

    @ManyToOne
    @JoinColumn(name = "EstadoCuentaNuevoID", referencedColumnName = "EstadoCuentaID")
    private EstadoCuentaModel estadoCuentaNuevo;

    @Column(name = "UsuarioID")
    private String usuarioId;

    @Column(name = "Observaciones")
    private String observaciones;

    @Column(name = "FechaCambio")
    private LocalDate fechaCambio;
}
