package com.back.agricultorservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="tblagricultor")
public class TblAgricultorModel {

    @Id
    @Column(name = "NitAgricultor")
    private String nitAgricultor;

    @Column(name = "Nombre")
    private String Nombre;

    @Column(name = "Activo")
    private boolean activo = true;

    @Column(name = "Observaciones")
    private String Observaciones;

    @Column(name = "FechaCreacion")
    private LocalDateTime FechaCreacion;

    @Column(name = "UsuarioCreacion")
    private String UsuarioCreacion;

    @Column(name = "FechaModificacion")
    private LocalDateTime FechaModificacion;

    @Column(name = "UsuarioModificacion")
    private String UsuarioModificacion;
}
