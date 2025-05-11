package com.back.agricultorservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "parcialidad")
public class ParcialidadModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ParcialidadID")
    private int parcialidad;

    @ManyToOne
    @JoinColumn(name = "SolicitudID", referencedColumnName = "SolicitudID")
    private SolicitudModel solicitud;

    @ManyToOne
    @JoinColumn(name = "PlacaTransporte", referencedColumnName = "PlacaTransporte")
    private TransporteModel transporte;

    @Column(name = "NombreTransportista")
    private String nombreTransportista;

    @ManyToOne
    @JoinColumn(name = "CuiTransportista", referencedColumnName = "CuiTransportista")
    private TransportistaModel transportista;

    @ManyToOne
    @JoinColumn(name = "MedidaPesoID", referencedColumnName = "MedidaPesoID")
    private MedidaPesoModel medidaPeso;

    @Column(name = "FechaRecepcionParcialidad")
    private LocalDateTime fechaRecepcionParcialidad;

    @Column(name = "PesoEnviado")
    private float pesoEnviado;

    @Column(name = "PesoBascula")
    private float pesoBascula;

    @Column(name = "DiferenciaPeso")
    private float diferenciaPeso;

    @Column(name = "FechaPesoBascula")
    private LocalDateTime fechaPesoBascula;

    @Column(name = "Activo")
    private boolean activo;

    @Column(name = "Observaciones")
    private String observaciones;

    @Column(name = "UsuarioCreacion")
    private String usuarioCreacion;

    @Column(name = "UsuarioModificacion")
    private String usuarioModificacion;
}
