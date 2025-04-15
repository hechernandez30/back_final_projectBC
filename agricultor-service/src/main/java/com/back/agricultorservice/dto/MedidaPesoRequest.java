package com.back.agricultorservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MedidaPesoRequest {
    private Long id;
    private String nombrePeso;
    private String abreviaturaPeso;
}
