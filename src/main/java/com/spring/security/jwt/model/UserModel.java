package com.spring.security.jwt.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "usuario")
public class UserModel {
    @Column(name = "UsuarioID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer user_id;

    @Column(name = "Nit")
    private String nit;

    @Column(name = "Nombre")
    String nombre;

    @Column(name = "Usuario")
    String usuario;

    @Column(name = "Contrasena")
    String contrasena;

    @Column(name = "Rol")
    String rol;

    @Column(name = "Observaciones")
    String observaciones;

    @Column(name = "Activo")
    boolean activo = true;

    @Column(name = "FechaCreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "UsuarioCreacion")
    private String usuarioCreacion;

    @Column(name = "FechaModificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "UsuarioModificacion")
    private String usuarioModificacion;
}
