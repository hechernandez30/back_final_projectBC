package com.back.basculaservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cuenta")
public class CuentaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CuentaID")
    private Integer CuentaId;
    private Integer numeroCuenta = 0;
    private String nitAgricultor;
    private Integer estadoCuentaId;
    private float pesoAcordado;
    private Integer cantParcialidades;
    private float pesoCadaParcialidad;
    private String medidaPeso;
    private String observaciones;
    private boolean activo = true;
    private LocalDateTime fechaCreacion;
    private String usuarioCreacion;
    private LocalDateTime fechaModificacion;
    private String usuarioModificacion;
}
