package com.spring.security.jwt.model;

import jakarta.persistence.*;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "transportistaautorizado")
public class TransportistaAutorizadoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TransportistaAutorizadoID")
    private int transportistaAutorizadoId;

    @Column(name = "CuiTransportista")
    private String cuiTransportista;

    @Column(name = "NombreCompleto")
    private String nombreCompleto;

    @Column(name = "FechaNacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "TipoLicencia")
    private String tipoLicencia;

    @Column(name = "FechaVencimientoLicencia")
    private LocalDate fechaVencimientoLicencia;

    @Column(name = "Activo")
    private boolean activo = true;

    @Column(name = "Observaciones")
    private String observaciones;

    @Column(name = "FechaCreaacion")
    private LocalDate fechaCreaacion;

    @Column(name = "UsuarioCreacion")
    private String usuarioCreacion;

    @Column(name = "FechaModificacion")
    private LocalDate fechaModificacion;

    @Column(name = "UsuarioModificacion")
    private String usuarioModificacion;
}
