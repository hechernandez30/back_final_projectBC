package com.spring.security.jwt.dto;

import com.spring.security.jwt.model.UserModel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransporteRequest {
    private String placaTransporte;
    private UserModel nitAgricultor;
    private String TipoPlaca;
    private String Marca;
    private String Color;
    private String Linea;
    private Integer Modelo;
    private String Observaciones;
}
