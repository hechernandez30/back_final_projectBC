package com.back.basculaservice.service;

import com.back.basculaservice.dto.Request.BoletaRequest;
import com.back.basculaservice.dto.Response.BoletaResponse;
import com.back.basculaservice.model.BoletaModel;
import com.back.basculaservice.repository.BoletaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoletaService {
    private final BoletaRepository boletaRepository;

    private BoletaResponse toResponse(BoletaModel model) {
        BoletaResponse dto = new BoletaResponse();
        BeanUtils.copyProperties(model, dto);
        return dto;
    }

    public BoletaResponse crear(BoletaRequest request, String usuario) {
        BoletaModel model = new BoletaModel();
        model.setParcialidadId(request.getParcialidadId());
        model.setMonto(request.getMonto());
        model.setObservaciones(request.getObservaciones());
        model.setFechaCreacion(LocalDate.now());
        model.setUsuarioCreacion(usuario);
        model.setActivo(true);
        return toResponse(boletaRepository.save(model));
    }

    public BoletaResponse actualizar(int id, BoletaRequest request, String usuario) {
        BoletaModel model = boletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Boleta no encontrada"));
        model.setParcialidadId(request.getParcialidadId());
        model.setMonto(request.getMonto());
        model.setObservaciones(request.getObservaciones());
        model.setFechaModificacion(LocalDate.now());
        model.setUsuarioModificacion(usuario);
        return toResponse(boletaRepository.save(model));
    }

    public void eliminar(int id, String usuario) {
        BoletaModel model = boletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Boleta no encontrada"));
        model.setActivo(false);
        model.setFechaModificacion(LocalDate.now());
        model.setUsuarioModificacion(usuario);
        boletaRepository.save(model);
    }

    public List<BoletaResponse> listar() {
        return boletaRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
