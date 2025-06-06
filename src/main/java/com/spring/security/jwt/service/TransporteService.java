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
import java.util.NoSuchElementException;
import java.util.Optional;
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
    public TransporteResponse crear(TransporteRequest request, String usuarioCreacion) {
        // Extraer el NIT del UserModel
        if (request.getNitAgricultor() == null || request.getNitAgricultor().getNit() == null) {
            throw new IllegalArgumentException("El NIT del agricultor es obligatorio.");
        }

        String nit = request.getNitAgricultor().getNit();

        UserModel agricultor = userRepository.findByNit(nit)
                .orElseThrow(() -> new IllegalArgumentException("El agricultor con NIT " + nit + " no existe."));

        // Validar existencia de placa
        Optional<TransporteModel> existente = transporteRepository.findById(request.getPlacaTransporte());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un transporte con esa placa.");
        }

        TransporteModel transporte = new TransporteModel();
        transporte.setPlacaTransporte(request.getPlacaTransporte());
        transporte.setNitAgricultor(agricultor); // ✅ Aquí asignamos un objeto persistido
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
        return toResponse(guardado);
    }


    @Transactional
    public TransporteResponse modificar(String placa, TransporteRequest request, String usuario) {
        TransporteModel model = transporteRepository.findById(placa)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró transporte con placa: " + placa));

        if (!model.isActivo()) {
            throw new IllegalStateException("No se puede modificar un transporte inactivo.");
        }

        if(request.getObservaciones() != null) {
            model.setObservaciones(request.getObservaciones());
        }

        //model.setPlacaTransporte(request.getPlacaTransporte());
        //model.setNitAgricultor(request.getNitAgricultor());
        //model.setTipoPlaca(request.getTipoPlaca());
        //model.setMarca(request.getMarca());
        //model.setColor(request.getColor());
        //model.setLinea(request.getLinea());
        //model.setModelo(request.getModelo());
        model.setDisponible(request.isDisponible());
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
        dto.setDisponible(model.isDisponible());
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

    public TransporteResponse obtenerPorPlaca(String placa) {
        TransporteModel transporte = transporteRepository.findById(placa)
                .orElseThrow(() -> new NoSuchElementException("Transporte no encontrado con placa: " + placa));
        return toResponse(transporte);
    }

}
