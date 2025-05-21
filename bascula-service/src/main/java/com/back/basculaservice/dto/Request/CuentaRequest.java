package com.back.basculaservice.dto.Request;

import lombok.Data;

@Data
public class CuentaRequest {
    private Integer numeroCuenta;
    private String nitAgricultor;
    private Integer estadoCuenta;
    private float pesoAcordado;
    private Integer cantParcialidades;
    private float pesoCadaParcialidad;
    private String medidaPeso;
    private String observaciones;
}
