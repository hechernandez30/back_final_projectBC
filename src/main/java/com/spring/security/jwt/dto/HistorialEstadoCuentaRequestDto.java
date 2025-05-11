package com.spring.security.jwt.dto;

import com.spring.security.jwt.model.CuentaModel;
import com.spring.security.jwt.model.EstadoCuentaModel;
import com.spring.security.jwt.model.UserModel;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HistorialEstadoCuentaRequestDto {
    private CuentaModel cuentaId;
    private EstadoCuentaModel estadoCuentaAnterior;
    private EstadoCuentaModel estadoCuentaNuevo;
    private String usuarioId;
    private String observaciones;
}
