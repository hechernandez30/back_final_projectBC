package com.spring.security.jwt.dto;

import com.spring.security.jwt.model.CuentaModel;
import com.spring.security.jwt.model.EstadoCuentaModel;
import com.spring.security.jwt.model.UserModel;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HistorialEstadoCuentaResponseDto {
    private Integer historialEstadoCuentaId;
    private CuentaModel cuentaId;
    private EstadoCuentaModel estadoCuentaAnteriorNombre;
    private EstadoCuentaModel estadoCuentaNuevoNombre;
    private String usuarioId;
    private String observaciones;
    private LocalDate fechaCambio;
}
