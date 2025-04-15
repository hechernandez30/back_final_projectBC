package com.back.agricultorservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TblAgricultorResponse {
    private String nitAgricultor;
    private String nombre;
    private boolean activo = true;
    private String observaciones;
    private LocalDate fechaCreacion;
    private String usuarioCreacion;
    private LocalDate fechaModificacion;
    private String usuarioModificacion;
}
