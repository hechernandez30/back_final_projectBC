package com.spring.security.jwt.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "cuenta")
public class CuentaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CuentaID")
    private int cuentaId;
    @Column(name = "NumeroCuenta")
    private Integer numeroCuenta = 0;
    @ManyToOne
    @JoinColumn(name = "NitAgricultor", referencedColumnName = "Nit")
    private UserModel agricultor;
    @ManyToOne
    @JoinColumn(name = "EstadoCuentaID", referencedColumnName = "EstadoCuentaID")
    private EstadoCuentaModel estadoCuenta;
    @Column(name = "PesoAcordado")
    private Float pesoAcordado;
    @Column(name = "CantParcialidades")
    private Integer cantParcialidades;
    @Column(name = "DiferenciaTotal")
    private Float diferenciaTotal;
    @Column(name = "Tolerancia")
    private String tolerancia;
    @Column(name = "PesoCadaParcialidad")
    private Float pesoCadaParcialidad;
    @Column(name = "MedidaPeso")
    private String medidaPeso;
    @Column(name = "Observaciones")
    private String observaciones;
    @Column(name = "FechaCreacion")
    private LocalDate fechaCreacion;
    @Column(name = "UsuarioCreacion")
    private String usuarioCreacion;
    @Column(name = "FechaModificacion")
    private LocalDate fechaModificacion;
    @Column(name = "UsuarioModificacion")
    private String usuarioModificacion;
}
