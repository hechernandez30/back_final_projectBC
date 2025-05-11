package com.back.basculaservice.dto.Response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MedidaPesoResponse {
    private Long id;
    private String nombrePeso;
    private String abreviaturaPeso;
    private boolean activo = true;
    private LocalDate fechaCreacion;
    private String usuarioCreacion;
    private LocalDate fechaModificacion;
    private String usuarioModificacion;
}
