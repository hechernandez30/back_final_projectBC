package com.spring.security.jwt.dto;

import lombok.Data;

@Data
public class UserRequestDto {
    private String nit;
    private String nombre;
    private String usuario;
    private String contrasena;
    private String rol;
    private String observaciones;
}
