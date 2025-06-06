package com.back.agricultorservice.service;

import com.back.agricultorservice.dto.TransportistaRequest;
import com.back.agricultorservice.dto.TransportistaResponse;
import com.back.agricultorservice.model.TransportistaModel;
import com.back.agricultorservice.repository.TransportistaRepository;
import com.spring.security.jwt.dto.TransportistaAutorizadoRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransportistaService {
    private final TransportistaRepository transportistaRepository;

    @Autowired
    private RestTemplate restTemplate;
    @Transactional
    public TransportistaResponse crearTransportista(TransportistaRequest request, String usuarioCreacion) {
        // Validar que no se registren CUI duplicados
        transportistaRepository.findById(request.getCuiTransportista()).ifPresent(existing -> {
            if (existing.isActivo()) {
                throw new IllegalArgumentException("Ya existe un transportista activo con ese CUI.");
            } else {
                throw new IllegalArgumentException("El CUI ingresado ya está registrado pero el transportista está inactivo.");
            }
        });

        // 🌐 Primero crear en Beneficio
        boolean creadoEnBeneficio = enviarABeneficio(request, usuarioCreacion);
        if (!creadoEnBeneficio) {
            throw new RuntimeException("No se pudo registrar el transportista en el módulo Beneficio.");
        }

        // 📝 Si Beneficio fue exitoso, creamos en Agricultor
        TransportistaModel transportista = new TransportistaModel();
        transportista.setCuiTransportista(request.getCuiTransportista());
        transportista.setNitAgricultor(request.getNitAgricultor());
        transportista.setNombreCompleto(request.getNombreCompleto());
        transportista.setFechaNacimiento(request.getFechaNacimiento());
        transportista.setTipoLicencia(request.getTipoLicencia());
        transportista.setFechaVencimientoLicencia(request.getFechaVencimientoLicencia());
        transportista.setObservaciones(request.getObservaciones());
        transportista.setFechaCreacion(LocalDateTime.now());
        transportista.setUsuarioCreacion(usuarioCreacion);

        TransportistaModel transportistaGuardado = transportistaRepository.save(transportista);
        return convertirATransportistaResponse(transportistaGuardado);
    }

    //Convertir usando DTO
    private TransportistaResponse convertirATransportistaResponse(TransportistaModel transportista) {
        TransportistaResponse response = new TransportistaResponse();
        response.setCuiTransportista(transportista.getCuiTransportista());
        response.setNitAgricultor(transportista.getNitAgricultor());
        response.setNombreCompleto(transportista.getNombreCompleto());
        response.setFechaNacimiento(transportista.getFechaNacimiento());
        response.setTipoLicencia(transportista.getTipoLicencia());
        response.setFechaVencimientoLicencia(transportista.getFechaVencimientoLicencia());
        response.setObservaciones(transportista.getObservaciones());
        response.setActivo(transportista.isActivo());
        return response;
    }

    private boolean enviarABeneficio(TransportistaRequest request, String usuarioCreacion) {
        try {
            String url = "http://localhost:3000/api/transportistas/crear";

            TransportistaAutorizadoRequestDto dto = new TransportistaAutorizadoRequestDto();
            dto.setCuiTransportista(request.getCuiTransportista());
            dto.setNitAgricultor(request.getNitAgricultor());
            dto.setNombreCompleto(request.getNombreCompleto());
            dto.setFechaNacimiento(request.getFechaNacimiento());
            dto.setTipoLicencia(request.getTipoLicencia());
            dto.setFechaVencimientoLicencia(request.getFechaVencimientoLicencia());
            dto.setObservaciones(request.getObservaciones());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Usuario", usuarioCreacion != null ? usuarioCreacion : "sistema");

            HttpEntity<TransportistaAutorizadoRequestDto> entity = new HttpEntity<>(dto, headers);

            restTemplate.postForEntity(url, entity, Void.class);
            return true;
        } catch (Exception ex) {
            // Puedes loguear el error si quieres ver el detalle
            System.err.println("Error al sincronizar con Beneficio: " + ex.getMessage());
            return false;
        }
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
