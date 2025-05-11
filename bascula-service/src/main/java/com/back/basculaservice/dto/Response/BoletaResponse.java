package com.back.basculaservice.dto.Response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BoletaResponse {
    private int boletaId;
    private int parcialidadId;
    private float monto;
    private String observaciones;
    private boolean activo = true;
    private LocalDate FechaCreacion;
    private String UsuarioCreacion;
    private LocalDate FechaModificacion;
    private String UsuarioModificacion;
}
