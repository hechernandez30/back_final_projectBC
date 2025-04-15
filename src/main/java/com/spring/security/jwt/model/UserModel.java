package com.spring.security.jwt.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Usuario")
public class UserModel {
    @Column(name = "UsuarioID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer user_id;
    @Column(name = "Nombre")
    String nombre;
    @Column(name = "Contrasena")
    String contrasena;
    @Column(name = "Rol")
    String rol;
}
