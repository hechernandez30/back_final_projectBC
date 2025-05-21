package com.back.basculaservice.dto.Response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CuentaResponse {
    private Integer cuentaId;
    private Integer numeroCuenta;
    private String nitAgricultor;
    private Integer estadoCuenta;
    private float pesoAcordado;
    private Integer cantParcialidades;
    private float pesoCadaParcialidad;
    private String medidaPeso;
    private String observaciones;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private String usuarioCreacion;
    private LocalDateTime fechaModificacion;
    private String usuarioModificacion;
}
