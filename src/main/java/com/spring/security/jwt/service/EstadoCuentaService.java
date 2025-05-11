package com.spring.security.jwt.service;

import com.spring.security.jwt.dto.EstadoCuentaRequestDto;
import com.spring.security.jwt.dto.EstadoCuentaResponseDto;
import com.spring.security.jwt.model.EstadoCuentaModel;
import com.spring.security.jwt.repository.EstadoCuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstadoCuentaService {
    private final EstadoCuentaRepository estadoCuentaRepository;

    public EstadoCuentaResponseDto crearEstadoCuenta(EstadoCuentaRequestDto request, String usuarioCreacion) {
        Optional<EstadoCuentaModel> existente = estadoCuentaRepository.findByNombreEstado(request.getNombreEstado());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("El nombre del estado ya existe");
        }

        EstadoCuentaModel nuevo = new EstadoCuentaModel();
        nuevo.setNombreEstado(request.getNombreEstado());
        nuevo.setDescripcionEstado(request.getDescripcionEstado());
        nuevo.setFechaCreacion(LocalDate.now());
        nuevo.setUsuarioCreacion(usuarioCreacion);
        nuevo.setActivo(true);

        EstadoCuentaModel guardado = estadoCuentaRepository.save(nuevo);
        return mapToDto(guardado);
    }

    public EstadoCuentaResponseDto actualizarEstadoCuenta(int id, EstadoCuentaRequestDto request, String usuarioModificacion) {
        Optional<EstadoCuentaModel> optional = estadoCuentaRepository.findById(id);
        if (optional.isEmpty() || !optional.get().isActivo()) {
            throw new RuntimeException("Estado de cuenta no encontrado o está inactivo.");
        }

        EstadoCuentaModel existente = optional.get();
        existente.setNombreEstado(request.getNombreEstado());
        existente.setDescripcionEstado(request.getDescripcionEstado());
        existente.setFechaModificacion(LocalDate.now());
        existente.setUsuarioModificacion(usuarioModificacion);

        EstadoCuentaModel actualizado = estadoCuentaRepository.save(existente);
        return mapToDto(actualizado);
    }

    public void eliminar(int id, String usuarioEliminacion) {
        Optional<EstadoCuentaModel> optional = estadoCuentaRepository.findById(id);
        if (optional.isEmpty() || !optional.get().isActivo()) {
            throw new RuntimeException("Estado de cuenta no encontrado o ya está eliminado.");
        }

        EstadoCuentaModel existente = optional.get();
        existente.setActivo(false);
        existente.setFechaModificacion(LocalDate.now());
        existente.setUsuarioModificacion(usuarioEliminacion);
        estadoCuentaRepository.save(existente);
    }

    public List<EstadoCuentaResponseDto> listarActivos() {
        return estadoCuentaRepository.findByActivoTrue()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private EstadoCuentaResponseDto mapToDto(EstadoCuentaModel model) {
        EstadoCuentaResponseDto dto = new EstadoCuentaResponseDto();
        dto.setEstadoCuentaId(model.getEstadoCuentaId());
        dto.setNombreEstado(model.getNombreEstado());
        dto.setDescripcionEstado(model.getDescripcionEstado());
        dto.setFechaCreacion(model.getFechaCreacion());
        dto.setUsuarioCreacion(model.getUsuarioCreacion());
        dto.setFechaModificacion(model.getFechaModificacion());
        dto.setUsuarioModificacion(model.getUsuarioModificacion());
        return dto;
    }
}
