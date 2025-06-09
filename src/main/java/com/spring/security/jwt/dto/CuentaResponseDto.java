package com.spring.security.jwt.dto;
import com.spring.security.jwt.model.UserModel;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CuentaResponseDto {
    private int cuentaId;
    private int numeroCuenta;
    private UserModel nitAgricultor;
    private String nitAgricultorNit;
    private String estadoCuenta;
    private Integer estadoCuentaId;
    private Float pesoAcordado;
    private Integer cantParcialidades;
    private Float diferenciaTotal;
    private String tolerancia;
    private Float pesoCadaParcialidad;
    private String medidaPeso;
    private String observaciones;
    private LocalDate fechaCreacion;
    private String usuarioCreacion;
    private LocalDate fechaModificacion;
    private String usuarioModificacion;
}
