package com.back.basculaservice.dto.Request;

import lombok.Data;

@Data
public class MedidaPesoRequest {
    private Long id;
    private String nombrePeso;
    private String abreviaturaPeso;
}
