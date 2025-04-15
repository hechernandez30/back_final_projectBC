package com.back.agricultorservice.service;

import com.back.agricultorservice.dto.SolicitudResponse;
import com.back.agricultorservice.dto.TransportistaRequest;
import com.back.agricultorservice.dto.TransportistaResponse;
import com.back.agricultorservice.model.SolicitudModel;
import com.back.agricultorservice.model.TblAgricultorModel;
import com.back.agricultorservice.model.TransportistaModel;
import com.back.agricultorservice.repository.TblAgricultorRepository;
import com.back.agricultorservice.repository.TransportistaRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransportistaService {
    private final TransportistaRepository transportistaRepository;
    private final TblAgricultorRepository tblAgricultorRepository;
    @Transactional
    public TransportistaResponse crearTransportista(TransportistaRequest request, String usuarioCreacion){

        //Validar si existe el agricultor antes de guardar.
        TblAgricultorModel agricultor = tblAgricultorRepository.findById(request.getNitAgricultor())
                .orElseThrow(() -> new IllegalArgumentException("Agricultor no encontrado con NIT: " + request.getNitAgricultor()));
        //Validar que no se registren CUI duplicados de transportistaas
        transportistaRepository.findById(request.getCuiTransportista()).ifPresent(existing -> {
            if (existing.isActivo()) {
                throw new IllegalArgumentException("Ya existe un transportista activo con ese CUI.");
            } else {
                throw new IllegalArgumentException("El CUI ingresado ya está registrado pero el transportista está inactivo.");
            }
        });
        //Crear el transportista
        TransportistaModel transportista = new TransportistaModel();
        transportista.setCuiTransportista(request.getCuiTransportista());
        transportista.setAgricultor(agricultor);
        transportista.setNombreCompleto(request.getNombreCompleto());
        transportista.setFechaNacimiento(request.getFechaNacimiento());
        transportista.setTipoLicencia(request.getTipoLicencia());
        transportista.setFechaVencimientoLicencia(request.getFechaVencimientoLicencia());
        transportista.setObservaciones(request.getObservaciones());
        transportista.setFechaCreacion(LocalDateTime.now());
        transportista.setUsuarioCreacion(usuarioCreacion);
        TransportistaModel transportistaGuardada = transportistaRepository.save(transportista);
        return convertirATransportistaResponse(transportistaGuardada);
    }
    //Convertir usando DTO
    private TransportistaResponse convertirATransportistaResponse(TransportistaModel transportista) {
        TransportistaResponse response = new TransportistaResponse();
        response.setCuiTransportista(transportista.getCuiTransportista());
        response.setNitAgricultor(transportista.getAgricultor().getNitAgricultor());
        response.setNombreCompleto(transportista.getNombreCompleto());
        response.setFechaNacimiento(transportista.getFechaNacimiento());
        response.setTipoLicencia(transportista.getTipoLicencia());
        response.setFechaVencimientoLicencia(transportista.getFechaVencimientoLicencia());
        response.setObservaciones(transportista.getObservaciones());
        return response;
    }
    // Metodo para obtener un transportista por CUI
    public TransportistaResponse obtenerTransportistaPorCui(String cui) {
        TransportistaModel transportista = transportistaRepository.findById(cui)
                .orElseThrow(() -> new IllegalArgumentException("Transportista no encontrado con ID: " + cui));
        return convertirATransportistaResponse(transportista);
    }
    //Metodo para actualizar un transportista usando su CUI
    @Transactional
    public TransportistaResponse actualizarTransportista(String cui, TransportistaRequest request) {
        TransportistaModel existente = transportistaRepository.findById(cui)
                .orElseThrow(() -> new IllegalArgumentException("Transportista no encontrado con ese CUI."));

        existente.setNombreCompleto(request.getNombreCompleto());
        existente.setFechaNacimiento(request.getFechaNacimiento());
        existente.setTipoLicencia(request.getTipoLicencia());
        existente.setFechaVencimientoLicencia(request.getFechaVencimientoLicencia());
        existente.setObservaciones(request.getObservaciones());
        existente.setActivo(request.isActivo());
        existente.setFechaModificacion(LocalDateTime.now());
        existente.setUsuarioModificacion(request.getUsuarioModificacion());

        TransportistaModel actualizado = transportistaRepository.save(existente);
        return convertirATransportistaResponse(actualizado);
    }
    //Metodo para eliminar un trasnsportista de forma logica
    @Transactional
    public void eliminarTransportista(String cui) {
        TransportistaModel transportista = transportistaRepository.findById(cui)
                .orElseThrow(() -> new IllegalArgumentException("Transportista no encontrado con ese CUI."));

        if (!transportista.isActivo()) {
            throw new IllegalStateException("El transportista ya está inactivo.");
        }
        transportista.setActivo(false);
        transportista.setFechaModificacion(LocalDateTime.now());
        // transportista.setUsuarioModificacion(usuario); // si lo tienes disponible

        transportistaRepository.save(transportista);
    }
    //Metodo para listar todos los trasnsportistas activos
    public List<TransportistaResponse> obtenerTransportistasActivos() {
        List<TransportistaModel> activos = transportistaRepository.findAllByActivoTrue();
        return activos.stream()
                .map(this::convertirATransportistaResponse)
                .toList();
    }

}
