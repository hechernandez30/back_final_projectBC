package com.spring.security.jwt.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDto {
    private Integer user_id;
    private String nit;
    private String nombre;
    private String usuario;
    private String rol;
    private String observaciones;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private String usuarioCreacion;
    private LocalDateTime fechaModificacion;
    private String usuarioModificacion;
}
