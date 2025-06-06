package com.spring.security.jwt.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "transporte")
public class TransporteModel {
    @Id
    @Column(name = "PlacaTransporte")
    private String placaTransporte;
    @ManyToOne
    @JoinColumn(name = "NitAgricultor", referencedColumnName = "Nit")
    private UserModel nitAgricultor;
    @Column(name = "TipoPlaca", nullable = true)
    private String TipoPlaca;
    @Column(name = "Marca", nullable = true)
    private String Marca;
    @Column(name = "Color", nullable = true)
    private String Color;
    @Column(name = "Linea", nullable = true)
    private String Linea;
    @Column(name = "Modelo", nullable = true)
    private Integer Modelo;
    @Column(name = "Activo", nullable = true)
    private boolean activo = true;
    @Column(name = "Disponible")
    private boolean disponible = true;
//    @Column(name = "Pesaje Asociado")
//    private int pesajeAsociado = 0;
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
