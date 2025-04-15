package com.back.agricultorservice.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SolicitudRequest {
    //@NotBlank(message = "NIT del agricultor es obligatorio")
    private String nitAgricultor;

//    @NotBlank(message = "Placa del transporte es obligatoria")
//    @Pattern(regexp = "^[A-Z0-9]{6,10}$", message = "Formato de placa inválido")
    private String placaTransporte;

//    @NotBlank(message = "CUI del transportista es obligatorio")
//    @Size(min = 13, max = 13, message = "El CUI debe tener 13 caracteres")
    private String cuiTransportista;

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
