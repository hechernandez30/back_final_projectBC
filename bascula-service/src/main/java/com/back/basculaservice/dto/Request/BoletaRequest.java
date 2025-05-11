package com.back.basculaservice.dto.Request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BoletaRequest {
    private int parcialidadId;
    private float monto;
    private String observaciones;
}
