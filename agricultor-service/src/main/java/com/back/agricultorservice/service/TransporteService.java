package com.back.agricultorservice.service;

import com.back.agricultorservice.dto.TransporteRequest;
import com.back.agricultorservice.dto.TransporteResponse;
import com.back.agricultorservice.model.TblAgricultorModel;
import com.back.agricultorservice.model.TransporteModel;
import com.back.agricultorservice.repository.TblAgricultorRepository;
import com.back.agricultorservice.repository.TransporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransporteService {
    private final TransporteRepository transporteRepository;
    private final TblAgricultorRepository agricultorRepository;

    public TransporteResponse crearTransporte(TransporteRequest request, String usuarioCreacion) {
        //Validar existencia por placa, para evitar registros duplicados en la BD
        Optional<TransporteModel> existente = transporteRepository.findById(request.getPlacaTransporte());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un transporte con esa placa.");
        }
        TblAgricultorModel agricultor = agricultorRepository.findById(request.getNitAgricultor())
                .orElseThrow(() -> new IllegalArgumentException("Agricultor no encontrado."));

        TransporteModel transporte = new TransporteModel();
        transporte.setPlacaTransporte(request.getPlacaTransporte());
        transporte.setAgricultor(agricultor);
        transporte.setTipoPlaca(request.getTipoPlaca());
        transporte.setMarca(request.getMarca());
        transporte.setColor(request.getColor());
        transporte.setLinea(request.getLinea());
        transporte.setModelo(request.getModelo());
        transporte.setObservaciones(request.getObservaciones());
        transporte.setActivo(true);
        transporte.setFechaCreacion(LocalDateTime.now());
        transporte.setUsuarioCreacion(usuarioCreacion);
        TransporteModel guardado = transporteRepository.save(transporte);
        return convertirAResponse(guardado);
    }

    public TransporteResponse actualizarTransporte(String placa, TransporteRequest request, String usuarioModificacion) {
        TblAgricultorModel agricultor = agricultorRepository.findById(request.getNitAgricultor())
                .orElseThrow(() -> new IllegalArgumentException("Agricultor no encontrado."));
        TransporteModel transporte = transporteRepository.findById(placa)
                .orElseThrow(() -> new IllegalArgumentException("Transporte no encontrado"));
        if (!transporte.isActivo()) {
            throw new IllegalArgumentException("No se puede modificar un trasnporte inactivo.");
        }

        transporte.setAgricultor(agricultor);
        transporte.setTipoPlaca(request.getTipoPlaca());
        transporte.setMarca(request.getMarca());
        transporte.setColor(request.getColor());
        transporte.setLinea(request.getLinea());
        transporte.setModelo(request.getModelo());
        transporte.setObservaciones(request.getObservaciones());
        transporte.setFechaModificacion(LocalDateTime.now());
        transporte.setUsuarioModificacion(usuarioModificacion);

        TransporteModel actualizado = transporteRepository.save(transporte);
        return convertirAResponse(actualizado);
    }

    public void eliminarTransporte(String placa) {
        TransporteModel transporte = transporteRepository.findById(placa)
                .orElseThrow(() -> new IllegalArgumentException("Transporte no encontrado"));
        transporte.setActivo(false);
        transporteRepository.save(transporte);
    }

    public List<TransporteResponse> listarTransportesActivos() {
        return transporteRepository.findByActivoTrue().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    private TransporteResponse convertirAResponse(TransporteModel t) {
        TransporteResponse response = new TransporteResponse();
        response.setPlacaTransporte(t.getPlacaTransporte());
        response.setNitAgricultor(t.getAgricultor().getNitAgricultor());
        response.setTipoPlaca(t.getTipoPlaca());
        response.setMarca(t.getMarca());
        response.setColor(t.getColor());
        response.setLinea(t.getLinea());
        response.setModelo(t.getModelo());
        response.setActivo(t.isActivo());
        response.setObservaciones(t.getObservaciones());
        response.setFechaCreacion(t.getFechaCreacion().toLocalDate());
        response.setUsuarioCreacion(t.getUsuarioCreacion());
        response.setFechaModificacion(t.getFechaModificacion() != null ? t.getFechaModificacion().toLocalDate() : null);
        response.setUsuarioModificacion(t.getUsuarioModificacion());
        return response;
    }
}
