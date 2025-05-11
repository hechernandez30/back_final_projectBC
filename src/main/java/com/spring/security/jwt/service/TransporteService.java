package com.spring.security.jwt.service;

import com.spring.security.jwt.dto.TransporteRequest;
import com.spring.security.jwt.dto.TransporteResponse;
import com.spring.security.jwt.model.TransporteModel;
import com.spring.security.jwt.model.UserModel;
import com.spring.security.jwt.repository.TransporteRepository;
import com.spring.security.jwt.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransporteService {
    private final TransporteRepository transporteRepository;
    private final UserRepository userRepository;

    public List<TransporteResponse> listarActivos() {
        return transporteRepository.findByActivoTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TransporteResponse crear(TransporteRequest request, String usuario) {
        if (transporteRepository.existsById(request.getPlacaTransporte())) {
            throw new IllegalArgumentException("Ya existe un transporte con esta placa.");
        }

        // Validar que el agricultor (usuario) exista por NIT
        boolean agricultorExiste = userRepository.existsByNit(String.valueOf(request.getNitAgricultor()));
        if (!agricultorExiste) {
            throw new IllegalArgumentException("El agricultor con NIT " + request.getNitAgricultor() + " no existe.");
        }

        TransporteModel model = toModel(request);
        model.setActivo(true);
        model.setFechaCreacion(LocalDateTime.now());
        model.setUsuarioCreacion(usuario);

        return toResponse(transporteRepository.save(model));
    }

    @Transactional
    public TransporteResponse modificar(String placa, TransporteRequest request, String usuario) {
        TransporteModel model = transporteRepository.findById(placa)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró transporte con placa: " + placa));

        if (!model.isActivo()) {
            throw new IllegalStateException("No se puede modificar un transporte inactivo.");
        }

        model.setPlacaTransporte(request.getPlacaTransporte());
        model.setNitAgricultor(request.getNitAgricultor());
        model.setTipoPlaca(request.getTipoPlaca());
        model.setMarca(request.getMarca());
        model.setColor(request.getColor());
        model.setLinea(request.getLinea());
        model.setModelo(request.getModelo());
        model.setObservaciones(request.getObservaciones());
        model.setFechaModificacion(LocalDateTime.now());
        model.setUsuarioModificacion(usuario);

        return toResponse(transporteRepository.save(model));
    }

    @Transactional
    public void eliminar(String placa, String usuario) {
        TransporteModel model = transporteRepository.findById(placa)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró transporte con placa: " + placa));

        model.setActivo(false);
        model.setFechaModificacion(LocalDateTime.now());
        model.setUsuarioModificacion(usuario);

        transporteRepository.save(model);
    }

    private TransporteResponse toResponse(TransporteModel model) {
        TransporteResponse dto = new TransporteResponse();
        dto.setPlacaTransporte(model.getPlacaTransporte());
        dto.setNitAgricultor(model.getNitAgricultor());
        dto.setTipoPlaca(model.getTipoPlaca());
        dto.setMarca(model.getMarca());
        dto.setColor(model.getColor());
        dto.setLinea(model.getLinea());
        dto.setModelo(model.getModelo());
        dto.setActivo(model.isActivo());
        dto.setObservaciones(model.getObservaciones());
        dto.setFechaCreacion(model.getFechaCreacion());
        dto.setUsuarioCreacion(model.getUsuarioCreacion());
        dto.setFechaModificacion(model.getFechaModificacion());
        dto.setUsuarioModificacion(model.getUsuarioModificacion());
        return dto;
    }

    private TransporteModel toModel(TransporteRequest request) {
        TransporteModel model = new TransporteModel();
        model.setPlacaTransporte(request.getPlacaTransporte());
        model.setNitAgricultor(request.getNitAgricultor());
        model.setTipoPlaca(request.getTipoPlaca());
        model.setMarca(request.getMarca());
        model.setColor(request.getColor());
        model.setLinea(request.getLinea());
        model.setModelo(request.getModelo());
        model.setObservaciones(request.getObservaciones());
        return model;
    }
}
