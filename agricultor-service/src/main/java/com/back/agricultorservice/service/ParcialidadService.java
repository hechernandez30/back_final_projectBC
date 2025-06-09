package com.back.agricultorservice.service;

import com.back.agricultorservice.dto.ParcialidadRequest;
import com.back.agricultorservice.dto.ParcialidadResponse;
import com.back.agricultorservice.model.*;
import com.back.agricultorservice.repository.*;
import com.spring.security.jwt.dto.ParcialidadRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParcialidadService {
    private final ParcialidadRepository parcialidadRepository;
    private final SolicitudRepository solicitudRepository;
    private final TransporteRepository transporteRepository;
    private final TransportistaRepository transportistaRepository;
    private final MedidaPesoRepository medidaPesoRepository;

    public ParcialidadResponse crearParcialidad(ParcialidadRequest request, String usuarioCreacion) {
        validarReferencias(request);

        SolicitudModel solicitud = solicitudRepository.findById((long) request.getSolicitudId())
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada."));
        TransporteModel transporte = transporteRepository.findById(request.getPlacaTransporte())
                .orElseThrow(() -> new RuntimeException("Transporte no encontrado."));
        TransportistaModel transportista = transportistaRepository.findById(request.getCuiTransportista())
                .orElseThrow(() -> new RuntimeException("Transportista no encontrado."));
        MedidaPesoModel medida = medidaPesoRepository.findById(request.getMedidaPesoId())
                .orElseThrow(() -> new RuntimeException("Medida de peso no encontrada."));

        ParcialidadModel parcialidad = new ParcialidadModel();
        parcialidad.setSolicitud(solicitud);
        parcialidad.setTransporte(transporte);
        parcialidad.setNombreTransportista(request.getNombreTransportista());
        parcialidad.setTransportista(transportista);
        parcialidad.setMedidaPeso(medida);
        parcialidad.setFechaRecepcionParcialidad(LocalDateTime.now());
        parcialidad.setPesoEnviado(request.getPesoEnviado());
        parcialidad.setPesoBascula(request.getPesoBascula());
        parcialidad.setDiferenciaPeso(request.getDiferenciaPeso());
        parcialidad.setFechaPesoBascula(LocalDateTime.now());
        parcialidad.setUsuarioCreacion(usuarioCreacion);
        parcialidad.setActivo(true);
        parcialidad.setObservaciones(request.getObservaciones());

        ParcialidadModel guardada;

        // SINCRONIZACIÓN CON BENEFICIO:
        try {
            // 1. Primero intentamos enviar al Beneficio
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:3000/api/parcialidades/crear";

            // Construir el DTO que espera el Beneficio:
            ParcialidadRequestDto requestDto = new ParcialidadRequestDto();
            requestDto.setSolicitudId(request.getSolicitudId());
            requestDto.setPlacaTransporte(request.getPlacaTransporte());
            requestDto.setNombreTransportista(request.getNombreTransportista());
            requestDto.setCuiTransportista(request.getCuiTransportista());
            requestDto.setMedidaPesoId(request.getMedidaPesoId());
            requestDto.setFechaRecepcionParcialidad(request.getFechaRecepcionParcialidad());
            requestDto.setPesoEnviado(request.getPesoEnviado());
            requestDto.setPesoBascula(request.getPesoBascula());
            requestDto.setDiferenciaPeso(request.getDiferenciaPeso());
            requestDto.setFechaPesoBascula(request.getFechaPesoBascula());
            requestDto.setObservaciones(request.getObservaciones());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Usuario", usuarioCreacion);

            HttpEntity<ParcialidadRequestDto> entity = new HttpEntity<>(requestDto, headers);

            restTemplate.postForEntity(url, entity, Void.class);

            // Si llegó aquí → sincronización OK → guardamos en Agricultor:
            guardada = parcialidadRepository.save(parcialidad);

        } catch (Exception ex) {
            // Si la sincronización falla → NO guardamos nada y lanzamos error:
            throw new RuntimeException("Error al sincronizar la parcialidad con el módulo Beneficio: " + ex.getMessage());
        }

        // Devolver la response local:
        return mapToResponse(guardada);
    }


    public void actualizarParcialidad(int id, ParcialidadRequest request, String usuarioModificacion) {
        ParcialidadModel parcialidad = parcialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parcialidad no encontrada."));

        SolicitudModel solicitud = solicitudRepository.findById((long) request.getSolicitudId())
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada."));
        TransporteModel transporte = transporteRepository.findById(request.getPlacaTransporte())
                .orElseThrow(() -> new RuntimeException("Transporte no encontrado."));
        TransportistaModel transportista = transportistaRepository.findById(request.getCuiTransportista())
                .orElseThrow(() -> new RuntimeException("Transportista no encontrado."));
        MedidaPesoModel medida = medidaPesoRepository.findById(request.getMedidaPesoId())
                .orElseThrow(() -> new RuntimeException("Medida de peso no encontrada."));

        validarReferencias(request);

        parcialidad.setSolicitud(solicitud);
        parcialidad.setTransporte(transporte);
        parcialidad.setNombreTransportista(request.getNombreTransportista());
        parcialidad.setTransportista(transportista);
        parcialidad.setMedidaPeso(medida);
        parcialidad.setFechaRecepcionParcialidad(LocalDateTime.now());
        parcialidad.setPesoEnviado(request.getPesoEnviado());
        parcialidad.setPesoBascula(request.getPesoBascula());
        parcialidad.setDiferenciaPeso(request.getDiferenciaPeso());
        parcialidad.setFechaPesoBascula(LocalDateTime.now());
        parcialidad.setUsuarioModificacion(usuarioModificacion);
        parcialidad.setObservaciones(request.getObservaciones());

        parcialidadRepository.save(parcialidad);
    }

    public void eliminarParcialidad(int id, String usuarioEliminacion) {
        ParcialidadModel parcialidad = parcialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parcialidad no encontrada."));
        parcialidad.setActivo(false);
        parcialidad.setUsuarioModificacion(usuarioEliminacion);
        parcialidadRepository.save(parcialidad);
    }

    public List<ParcialidadResponse> obtenerTodas() {
        return parcialidadRepository.findAll().stream()
                .filter(ParcialidadModel::isActivo)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ParcialidadResponse obtenerPorId(int id) {
        ParcialidadModel parcialidad = parcialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parcialidad no encontrada."));
        return mapToResponse(parcialidad);
    }

    public List<ParcialidadResponse> listarPorSolicitud(int solicitudId) {
        SolicitudModel solicitud = solicitudRepository.findById(Long.valueOf(solicitudId))
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada."));
        return parcialidadRepository.findBySolicitudAndActivoTrue(solicitud).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public boolean verificarParcialidadesCompletadas(int solicitudId) {
        SolicitudModel solicitud = solicitudRepository.findById(Long.valueOf(solicitudId))
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada."));
        long actuales = parcialidadRepository.countBySolicitudAndActivoTrue(solicitud);
        return actuales >= solicitud.getCantParcialidades();
    }

    private void validarReferencias(ParcialidadRequest request) {
        solicitudRepository.findById((long) request.getSolicitudId())
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada."));

        transporteRepository.findById(request.getPlacaTransporte())
                .filter(TransporteModel::isActivo)
                .orElseThrow(() -> new RuntimeException("Transporte no encontrado o inactivo."));

        transportistaRepository.findById(request.getCuiTransportista())
                .filter(TransportistaModel::isActivo)
                .orElseThrow(() -> new RuntimeException("Transportista no encontrado o inactivo."));

        medidaPesoRepository.findById(request.getMedidaPesoId())
                .filter(MedidaPesoModel::isActivo)
                .orElseThrow(() -> new RuntimeException("Medida de peso no encontrada o inactiva."));
    }

    private ParcialidadResponse mapToResponse(ParcialidadModel model) {
        ParcialidadResponse dto = new ParcialidadResponse();

        BeanUtils.copyProperties(model, dto);

        if(model.getTransporte() != null) {
            dto.setPlacaTransporte(model.getTransporte().getPlacaTransporte());
        }
        return dto;
    }
}
