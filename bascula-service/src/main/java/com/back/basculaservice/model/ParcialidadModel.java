package com.back.basculaservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name= "parcialidad")
public class ParcialidadModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ParcialidadID")
    private int parcialidadId;
    @Column(name = "CuentaID")
    private int solicitudId;
    private String placaTransporte;
    private String nombreTransportista;
    private String cuiTransportista;
    private int medidaPesoId;
    private LocalDate fechaRecepcionParcialidad;
    private Float pesoEnviado;
    private Float pesoBascula;
    private Float diferenciaPeso;
    private LocalDate fechaPesoBascula;
    private boolean activo = true;
    private String observaciones;
    private String usuarioCreacion;
    private String usuarioModificacion;
}
