package com.back.agricultorservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="solicitud")
public class SolicitudModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SolicitudID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "NitAgricultor", referencedColumnName = "NitAgricultor")
    private TblAgricultorModel agricultor;

    @ManyToOne
    @JoinColumn(name = "PlacaTransporte", referencedColumnName = "PlacaTransporte")
    private TransporteModel transporte;

    @ManyToOne
    @JoinColumn(name = "CuiTransportista", referencedColumnName = "CuiTransportista")
    private TransportistaModel transportista;

    private Float pesoAcordado;
    private Integer cantParcialidades;
    private Float pesoCadaParcialidad;

    @ManyToOne
    @JoinColumn(name = "MedidaPesoID", referencedColumnName = "MedidaPesoID")
    private MedidaPesoModel medidaPeso;
    private String observaciones;
    private String estado;
    private LocalDateTime fechaCreacion;
    private String usuarioCreacion;
    private LocalDateTime fechaModificacion;
    private String usuarioModificacion;
}
