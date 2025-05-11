package com.back.basculaservice.dto.Request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ParcialidadRequest {
    private int solicitudId;
    private String placaTransporte;
    private String nombreTransportista;
    private String cuiTransportista;
    private int medidaPesoId;
    private LocalDate fechaRecepcionParcialidad;
    private Float pesoEnviado;
    private Float pesoBascula;
    private Float diferenciaPeso;
    private LocalDate fechaPesoBascula;
    private String observaciones;
}
