package com.spring.security.jwt.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ParcialidadRequestDto {
    private int solicitudId;
    private String placaTransporte;
    private String nombreTransportista;
    private String cuiTransportista;
    private int medidaPesoId;
    private LocalDate fechaRecepcionParcialidad;
    private Float pesoEnviado;
    private Float pesoBascula;
    private Float diferenciaPeso;
    private LocalDate fechaPesoBascula;
    private String observaciones;
}
