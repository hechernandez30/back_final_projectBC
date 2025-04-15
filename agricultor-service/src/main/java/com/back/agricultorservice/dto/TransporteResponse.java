package com.back.agricultorservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TransporteResponse {
    private String placaTransporte;
    private String nitAgricultor;
    private String tipoPlaca;
    private String marca;
    private String color;
    private String linea;
    private Integer modelo;
    private boolean activo = true;
    private String observaciones;
    private LocalDate fechaCreacion;
    private String usuarioCreacion;
    private LocalDate fechaModificacion;
    private String usuarioModificacion;
}
