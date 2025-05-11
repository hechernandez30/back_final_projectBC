package com.spring.security.jwt.dto;

import com.spring.security.jwt.model.UserModel;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransporteResponse {
    private String placaTransporte;
    private UserModel nitAgricultor;
    private String TipoPlaca;
    private String Marca;
    private String Color;
    private String Linea;
    private Integer Modelo;
    private boolean activo = true;
    private String Observaciones;
    private LocalDateTime FechaCreacion;
    private String UsuarioCreacion;
    private LocalDateTime FechaModificacion;
    private String UsuarioModificacion;
}
