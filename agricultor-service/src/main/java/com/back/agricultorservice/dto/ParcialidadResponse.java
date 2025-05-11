package com.back.agricultorservice.dto;

import com.back.agricultorservice.model.MedidaPesoModel;
import com.back.agricultorservice.model.SolicitudModel;
import com.back.agricultorservice.model.TransporteModel;
import com.back.agricultorservice.model.TransportistaModel;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ParcialidadResponse {
    private int parcialidadId;
    private SolicitudModel solicitud;
    private TransporteModel transporte;
    private String nombreTransportista;
    private TransportistaModel transportista;
    private MedidaPesoModel medidaPeso;
    private LocalDate fechaRecepcionParcialidad;
    private float pesoEnviado;
    private float pesoBascula;
    private float diferenciaPeso;
    private LocalDate fechaPesoBascula;
    private boolean activo;
    private String observaciones;
    private String usuarioCreacion;
    private String usuarioModificacion;
}
