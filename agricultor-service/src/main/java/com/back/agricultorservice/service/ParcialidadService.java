package com.back.agricultorservice.service;

import com.back.agricultorservice.dto.ParcialidadRequest;
import com.back.agricultorservice.dto.ParcialidadResponse;
import com.back.agricultorservice.model.*;
import com.back.agricultorservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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

        ParcialidadModel parcialidad = new ParcialidadModel();
        parcialidad.setSolicitud(request.getSolicitud());
        parcialidad.setTransporte(request.getTransporte());
        parcialidad.setNombreTransportista(request.getNombreTransportista());
        parcialidad.setTransportista(request.getTransportista());
        parcialidad.setMedidaPeso(request.getMedidaPeso());
        parcialidad.setFechaRecepcionParcialidad(LocalDateTime.now());
        parcialidad.setPesoEnviado(request.getPesoEnviado());
        parcialidad.setPesoBascula(request.getPesoBascula());
        parcialidad.setDiferenciaPeso(request.getDiferenciaPeso());
        parcialidad.setFechaPesoBascula(LocalDateTime.now());
        parcialidad.setUsuarioCreacion(usuarioCreacion);
        parcialidad.setActivo(true);
        parcialidad.setObservaciones(request.getObservaciones());

        return mapToResponse(parcialidadRepository.save(parcialidad));
    }

    public void actualizarParcialidad(int id, ParcialidadRequest request, String usuarioModificacion) {
        ParcialidadModel parcialidad = parcialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parcialidad no encontrada."));

        validarReferencias(request);

        parcialidad.setSolicitud(request.getSolicitud());
        parcialidad.setTransporte(request.getTransporte());
        parcialidad.setNombreTransportista(request.getNombreTransportista());
        parcialidad.setTransportista(request.getTransportista());
        parcialidad.setMedidaPeso(request.getMedidaPeso());
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
        solicitudRepository.findById(request.getSolicitud().getId())
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada."));

        TransporteModel transporte = transporteRepository.findById(request.getTransporte().getPlacaTransporte())
                .orElseThrow(() -> new RuntimeException("Transporte no encontrado."));
        if (!transporte.isActivo()) throw new RuntimeException("El transporte no está activo.");

        TransportistaModel transportista = transportistaRepository.findById(request.getTransportista().getCuiTransportista())
                .orElseThrow(() -> new RuntimeException("Transportista no encontrado."));
        if (!transportista.isActivo()) throw new RuntimeException("El transportista no está activo.");

        MedidaPesoModel medida = medidaPesoRepository.findById(Math.toIntExact(request.getMedidaPeso().getId()))
                .orElseThrow(() -> new RuntimeException("Medida de peso no encontrada."));
        if (!medida.isActivo()) throw new RuntimeException("La medida de peso no está activa.");
    }

    private ParcialidadResponse mapToResponse(ParcialidadModel model) {
        ParcialidadResponse dto = new ParcialidadResponse();
        BeanUtils.copyProperties(model, dto);
        return dto;
    }
}
