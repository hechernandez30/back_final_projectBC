package com.spring.security.jwt.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;
@Data
public class EstadoCuentaResponseDto {
    private int estadoCuentaId;
    private String nombreEstado;
    private String descripcionEstado;
    private boolean activo;
    private LocalDate fechaCreacion;
    private String usuarioCreacion;
    private LocalDate fechaModificacion;
    private String usuarioModificacion;
}
