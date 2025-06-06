package com.spring.security.jwt.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class TransportistaAutorizadoRequestDto {
    private String cuiTransportista;
    private String nitAgricultor;
    private String nombreCompleto;
    private LocalDate fechaNacimiento;
    private String tipoLicencia;
    private LocalDate fechaVencimientoLicencia;
    private String observaciones;
    private boolean disponible;
}
