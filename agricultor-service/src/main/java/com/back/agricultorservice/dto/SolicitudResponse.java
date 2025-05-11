package com.back.agricultorservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SolicitudResponse {
    private Long solicitudId;
    private int numeroCuenta;
    private String nitAgricultor;
    private Float pesoAcordado;
    private Integer cantParcialidades;
    private Float pesoCadaParcialidad;
    private String medidaPeso;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private String usuarioCreacion;
    private LocalDateTime fechaModificacion;
    private String usuarioModificacion;
    private String estado;

}
