package com.back.agricultorservice.service;

import com.back.agricultorservice.dto.SolicitudRequest;
import com.back.agricultorservice.dto.SolicitudResponse;
import com.back.agricultorservice.model.*;
import com.back.agricultorservice.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitudService {
    private final SolicitudRepository solicitudRepository;
    private final TblAgricultorRepository agricultorRepository;
    private final TransporteRepository transporteRepository;
    private final TransportistaRepository transportistaRepository;
    private final MedidaPesoRepository medidaPesoRepository;

    @Transactional
    public SolicitudResponse crearSolicitud(SolicitudRequest request, String usuarioCreacion) {
        //1. Validar agricultor activo
        TblAgricultorModel agricultor = agricultorRepository.findById(request.getNitAgricultor())
                .orElseThrow(() -> new IllegalArgumentException("Agricultor no encontrado con NIT: " + request.getNitAgricultor()));
        if (!agricultor.isActivo()) {
           throw new IllegalArgumentException("El agricultor no esta activo");
        }
        //2. Validar transporte activo y perteneciente al agricultor
        TransporteModel transporte = transporteRepository.findById(request.getPlacaTransporte())
                .orElseThrow(() -> new IllegalArgumentException("Transporte no encontrado con placca: " + request.getPlacaTransporte()));
        if (!transporte.isActivo()) {
            throw new IllegalArgumentException("El transporte está inactivo");
        }
        if (!transporte.getAgricultor().getNitAgricultor().equals(agricultor.getNitAgricultor())) {
            throw new IllegalArgumentException("El transporte no pertenece al agricultor");
        }
        //3. Validar transportista activo y perteneciente al agricultor
        TransportistaModel transportista = transportistaRepository.findById(request.getCuiTransportista())
                .orElseThrow(() -> new IllegalArgumentException("Transportista no encontrado con CUI: " + request.getCuiTransportista()));
        if (!transportista.isActivo()) {
            throw new IllegalArgumentException("El transportista está inactivo");
        }
        if (!transportista.getAgricultor().getNitAgricultor().equals(agricultor.getNitAgricultor())) {
            throw new IllegalArgumentException("El transportista no pertenece al agricultor");
        }
        //4. Validar medida de peso
        MedidaPesoModel medidaPeso = medidaPesoRepository.findById(request.getMedidaPesoId())
                .orElseThrow(() -> new IllegalArgumentException("Medida de peso no encontrada con ID: " + request.getMedidaPesoId()));
//        // 5. Validar coherencia de pesos (opcional)
        if (request.getPesoAcordado() != (request.getCantParcialidades() * request.getPesoCadaParcialidad())) {
            throw new IllegalArgumentException("El peso acordado no coincide con el cálculo de parcialidades");
        }
        // 6. Crear y guardar la solicitud
        SolicitudModel solicitud = new SolicitudModel();
        solicitud.setAgricultor(agricultor);
        solicitud.setTransporte(transporte);
        solicitud.setTransportista(transportista);
        solicitud.setMedidaPeso(medidaPeso);
        solicitud.setPesoAcordado(request.getPesoAcordado());
        solicitud.setCantParcialidades(request.getCantParcialidades());
        solicitud.setPesoCadaParcialidad(request.getPesoCadaParcialidad());
        solicitud.setObservaciones(request.getObservaciones());
        solicitud.setEstado(request.getEstado());
        solicitud.setFechaCreacion(LocalDateTime.now());
        solicitud.setUsuarioCreacion(usuarioCreacion);

        SolicitudModel solicitudGuardada = solicitudRepository.save(solicitud);
        return convertirASolicitudResponse(solicitudGuardada);
    }
    // Metodo para obtener todas las solicitudes de un agricultor
    public List<SolicitudResponse> listarSolicitudesPorAgricultor(String nitAgricultor) {
        return solicitudRepository.findByAgricultor_NitAgricultor(nitAgricultor)
                .stream()
                .map(this::convertirASolicitudResponse)
                .collect(Collectors.toList());
    }

    // Metodo para obtener una solicitud por ID
    public SolicitudResponse obtenerSolicitudPorId(Long id) {
        SolicitudModel solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada con ID: " + id));
        return convertirASolicitudResponse(solicitud);
    }
    private SolicitudResponse convertirASolicitudResponse(SolicitudModel solicitud) {
        SolicitudResponse response = new SolicitudResponse();
        response.setSolicitudId(solicitud.getId());
        response.setNitAgricultor(solicitud.getAgricultor().getNitAgricultor());
        response.setNombreAgricultor(solicitud.getAgricultor().getNombre());
        response.setPlacaTransporte(solicitud.getTransporte().getPlacaTransporte());
        response.setTipoTransporte(solicitud.getTransporte().getTipoPlaca() + " - " + solicitud.getTransporte().getMarca());
        response.setCuiTransportista(solicitud.getTransportista().getCuiTransportista());
        response.setNombreTransportista(solicitud.getTransportista().getNombreCompleto());
        response.setPesoAcordado(solicitud.getPesoAcordado());
        response.setCantParcialidades(solicitud.getCantParcialidades());
        response.setPesoCadaParcialidad(solicitud.getPesoCadaParcialidad());
        response.setMedidaPeso(solicitud.getMedidaPeso().getNombrePeso() + " (" + solicitud.getMedidaPeso().getAbreviaturaPeso() + ")");
        response.setObservaciones(solicitud.getObservaciones());
        response.setEstado(solicitud.getEstado());
        response.setFechaCreacion(solicitud.getFechaCreacion());
        return response;
    }
    //Metodo para listar todas las solicitudes
    public List<SolicitudResponse> obtenerTodasLasSolicitudes() {
        List<SolicitudModel> solicitudes = solicitudRepository.findAll();
        return solicitudes.stream().map(this::convertirASolicitudResponse).toList();
    }
}
