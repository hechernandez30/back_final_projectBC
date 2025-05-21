package com.back.agricultorservice.dto;

import com.back.agricultorservice.model.MedidaPesoModel;
import com.back.agricultorservice.model.SolicitudModel;
import com.back.agricultorservice.model.TransporteModel;
import com.back.agricultorservice.model.TransportistaModel;
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
    private float pesoEnviado;
    private float pesoBascula;
    private float diferenciaPeso;
    private LocalDate fechaPesoBascula;
    private String observaciones;
}
