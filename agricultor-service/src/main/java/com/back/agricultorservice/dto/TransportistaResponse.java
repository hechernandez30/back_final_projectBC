package com.back.agricultorservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class TransportistaResponse {
    private String cuiTransportista;
    private String nitAgricultor;
    private String nombreCompleto;
    private LocalDate fechaNacimiento;
    private String tipoLicencia;
    private LocalDate fechaVencimientoLicencia;
    private String observaciones;
    private boolean activo = true;
}
