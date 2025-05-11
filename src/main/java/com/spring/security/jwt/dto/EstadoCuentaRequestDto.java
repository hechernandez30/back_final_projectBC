package com.spring.security.jwt.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EstadoCuentaRequestDto {
    private String nombreEstado;
    private String descripcionEstado;
}
