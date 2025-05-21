package com.back.basculaservice.service;

import com.back.basculaservice.dto.Request.CuentaRequest;
import com.back.basculaservice.dto.Response.CuentaResponse;
import com.back.basculaservice.model.CuentaModel;
import com.back.basculaservice.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.BeanUtils;

@Service
@RequiredArgsConstructor
public class CuentaService {
    private final CuentaRepository cuentaRepository;

    public CuentaResponse crear(CuentaRequest dto, String usuario) {
        CuentaModel model = new CuentaModel();
        model.setNumeroCuenta(dto.getNumeroCuenta());
        model.setNitAgricultor(dto.getNitAgricultor());
        model.setEstadoCuentaId(dto.getEstadoCuenta());
        model.setPesoAcordado(dto.getPesoAcordado());
        model.setCantParcialidades(dto.getCantParcialidades());
        model.setPesoCadaParcialidad(dto.getPesoCadaParcialidad());
        model.setMedidaPeso(dto.getMedidaPeso());
        model.setObservaciones(dto.getObservaciones());
        model.setActivo(true);
        model.setFechaCreacion(LocalDateTime.now());
        model.setUsuarioCreacion(usuario);
        return toResponse(cuentaRepository.save(model));
    }

    public CuentaResponse actualizar(int id, CuentaRequest dto, String usuario) {
        CuentaModel model = cuentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        model.setNumeroCuenta(dto.getNumeroCuenta());
        model.setNitAgricultor(dto.getNitAgricultor());
        model.setEstadoCuentaId(dto.getEstadoCuenta());
        model.setPesoAcordado(dto.getPesoAcordado());
        model.setCantParcialidades(dto.getCantParcialidades());
        model.setPesoCadaParcialidad(dto.getPesoCadaParcialidad());
        model.setMedidaPeso(dto.getMedidaPeso());
        model.setObservaciones(dto.getObservaciones());
        model.setFechaModificacion(LocalDateTime.now());
        model.setUsuarioModificacion(usuario);

        return toResponse(cuentaRepository.save(model));
    }

    public void eliminar(int id, String usuario) {
        CuentaModel model = cuentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        model.setActivo(false);
        model.setFechaModificacion(LocalDateTime.now());
        model.setUsuarioModificacion(usuario);
        cuentaRepository.save(model);
    }

    public List<CuentaResponse> listarActivos() {
        return cuentaRepository.findByActivoTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    private CuentaResponse toResponse(CuentaModel model) {
        CuentaResponse dto = new CuentaResponse();
        BeanUtils.copyProperties(model, dto);
        return dto;
    }
}
