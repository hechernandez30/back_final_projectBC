package com.spring.security.jwt.dto;

import com.spring.security.jwt.model.EstadoCuentaModel;
import com.spring.security.jwt.model.UserModel;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CuentaRequestDto {
    private int numeroCuenta;
    private UserModel nitAgricultor;
    private EstadoCuentaModel estadoCuenta;
    private Float pesoAcordado;
    private Integer cantParcialidades;
    private Float pesoCadaParcialidad;
    private String medidaPeso;
    private String observaciones;
}
