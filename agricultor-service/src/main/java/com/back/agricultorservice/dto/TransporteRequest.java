package com.back.agricultorservice.dto;

import lombok.Data;

@Data
public class TransporteRequest {
    private String placaTransporte;
    private String nitAgricultor;
    private String tipoPlaca;
    private String marca;
    private String color;
    private String linea;
    private Integer modelo;
    private boolean disponible;
    private int pesajeAsociado;
    private String observaciones;
}
