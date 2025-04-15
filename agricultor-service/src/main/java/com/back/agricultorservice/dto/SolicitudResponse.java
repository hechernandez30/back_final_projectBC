package com.back.agricultorservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SolicitudResponse {
    private Long solicitudId;
    private String nitAgricultor;
    private String nombreAgricultor;
    private String placaTransporte;
    private String tipoTransporte;
    private String cuiTransportista;
    private String nombreTransportista;
    private Float pesoAcordado;
    private Integer cantParcialidades;
    private Float pesoCadaParcialidad;
    private String medidaPeso;
    private String observaciones;
    private String estado;
    private LocalDateTime fechaCreacion;
}
