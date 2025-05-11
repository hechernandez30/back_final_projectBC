package com.spring.security.jwt.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "estadocuenta")
public class EstadoCuentaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EstadoCuentaID")
    private int estadoCuentaId;

    @Column(name = "NombreEstado")
    private String nombreEstado;

    @Column(name = "Activo")
    private boolean activo;

    @Column(name = "DescripcionEstado")
    private String descripcionEstado;

    @Column(name = "FechaCreacion")
    private LocalDate fechaCreacion;

    @Column(name = "UsuarioCreacion")
    private String usuarioCreacion;

    @Column(name = "FechaModificacion")
    private LocalDate fechaModificacion;

    @Column(name = "UsuarioModificacion")
    private String usuarioModificacion;
}
