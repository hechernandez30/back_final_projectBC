package com.spring.security.jwt.service;

import com.spring.security.jwt.dto.TransportistaAutorizadoRequestDto;
import com.spring.security.jwt.dto.TransportistaAutorizadoResponseDto;
import com.spring.security.jwt.model.TransportistaAutorizadoModel;
import com.spring.security.jwt.repository.TransportistaAutorizadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransportistaAutorizadoService {
    private final TransportistaAutorizadoRepository repository;

    public TransportistaAutorizadoResponseDto crear(TransportistaAutorizadoRequestDto request, String usuario) {
        // Verificar si ya existe por CUI
        if (repository.findByCuiTransportista(request.getCuiTransportista()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un transportista con este CUI.");
        }

        TransportistaAutorizadoModel model = new TransportistaAutorizadoModel();
        model.setCuiTransportista(request.getCuiTransportista());
        model.setNitAgricultor(request.getNitAgricultor());
        model.setNombreCompleto(request.getNombreCompleto());
        model.setFechaNacimiento(request.getFechaNacimiento());
        model.setTipoLicencia(request.getTipoLicencia());
        model.setFechaVencimientoLicencia(request.getFechaVencimientoLicencia());
        model.setObservaciones(request.getObservaciones());
        model.setFechaCreacion(LocalDate.now());
        model.setUsuarioCreacion(usuario);
        model.setActivo(true);

        return toDto(repository.save(model));
    }

    public List<TransportistaAutorizadoResponseDto> listarActivos() {
        return repository.findByActivoTrue()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public TransportistaAutorizadoResponseDto buscarPorCui(String cui) {
        TransportistaAutorizadoModel model = repository.findByCuiTransportista(cui)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró un transportista con CUI: " + cui));
        return toDto(model);
    }

    public TransportistaAutorizadoResponseDto modificar(int id, TransportistaAutorizadoRequestDto request, String usuario) {
        TransportistaAutorizadoModel model = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transportista no encontrado con ID: " + id));

        model.setNombreCompleto(request.getNombreCompleto());
        model.setFechaNacimiento(request.getFechaNacimiento());
        model.setTipoLicencia(request.getTipoLicencia());
        model.setFechaVencimientoLicencia(request.getFechaVencimientoLicencia());
        model.setDisponible(request.isDisponible());
        model.setObservaciones(request.getObservaciones());
        model.setFechaModificacion(LocalDate.now());
        model.setUsuarioModificacion(usuario);

        return toDto(repository.save(model));
    }

    public void eliminarLogico(int id, String usuario) {
        TransportistaAutorizadoModel model = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transportista no encontrado con ID: " + id));

        model.setActivo(false);
        model.setFechaModificacion(LocalDate.now());
        model.setUsuarioModificacion(usuario);

        repository.save(model);
    }

    private TransportistaAutorizadoResponseDto toDto(TransportistaAutorizadoModel model) {
        TransportistaAutorizadoResponseDto dto = new TransportistaAutorizadoResponseDto();
        BeanUtils.copyProperties(model, dto);
        return dto;
    }

    public TransportistaAutorizadoResponseDto buscarPorId(int id) {
        TransportistaAutorizadoModel model = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transportista no encontrado"));
        return toDto(model);
    }
}
