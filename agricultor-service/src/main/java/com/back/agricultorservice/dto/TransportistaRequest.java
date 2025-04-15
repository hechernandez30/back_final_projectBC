package com.back.agricultorservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class TransportistaRequest {
    private String cuiTransportista;
    private String nitAgricultor;
    private String nombreCompleto;
    private LocalDate fechaNacimiento;
    private String tipoLicencia;
    private LocalDate fechaVencimientoLicencia;
    private boolean activo = true;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private String usuarioCreacion;
    private LocalDateTime fechaModificacion;
    private String usuarioModificacion;
}
