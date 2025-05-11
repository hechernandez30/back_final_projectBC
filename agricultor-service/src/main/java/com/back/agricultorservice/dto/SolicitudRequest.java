package com.back.agricultorservice.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SolicitudRequest {
    private int numeroCuenta;
    //@NotBlank(message = "NIT del agricultor es obligatorio")
    private String nitAgricultor;
//    @Positive(message = "El peso acordado debe ser mayor a 0")
    private Float pesoAcordado;
//    @Min(value = 1, message = "Debe haber al menos 1 parcialidad")
    private Integer cantParcialidades;
//    @Positive(message = "El peso por parcialiad debe ser mayor a 0")
    private Float pesoCadaParcialidad;
//    @NotNull(message = "La medida de peso es obligatoria")
    private Integer medidaPesoId;
//    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;
    private String estado;
}
