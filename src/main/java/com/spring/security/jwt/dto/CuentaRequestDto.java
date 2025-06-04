package com.spring.security.jwt.dto;

import com.spring.security.jwt.model.EstadoCuentaModel;
import com.spring.security.jwt.model.UserModel;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CuentaRequestDto {
    private int numeroCuenta;
    private String nitAgricultor;
    private Integer estadoCuentaId;
    private Float pesoAcordado;
    private Integer cantParcialidades;
    private Float pesoCadaParcialidad;
    private String medidaPeso;
    private String observaciones;
}
