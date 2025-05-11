package com.spring.security.jwt.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TransportistaAutorizadoResponseDto {
    private int transportistaAutorizadoId;
    private String cuiTransportista;
    private String nombreCompleto;
    private LocalDate fechaNacimiento;
    private String tipoLicencia;
    private LocalDate fechaVencimientoLicencia;
    private boolean activo;
    private String observaciones;
    private LocalDate fechaCreaacion;
    private String usuarioCreacion;
    private LocalDate fechaModificacion;
    private String usuarioModificacion;
}
