package com.back.basculaservice.service;

import com.back.basculaservice.dto.Request.MedidaPesoRequest;
import com.back.basculaservice.dto.Response.MedidaPesoResponse;
import com.back.basculaservice.model.MedidaPesoModel;
import com.back.basculaservice.repository.MedidaPesoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedidaPesoService {
    private final MedidaPesoRepository medidaPesoRepository;

    public MedidaPesoResponse crearMedidaPeso(MedidaPesoRequest request, String usuarioCreacion) {
        Optional<MedidaPesoModel> existente = medidaPesoRepository.findByAbreviaturaPeso(request.getAbreviaturaPeso());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe una medida de peso con esa abreviatura.");
        }

        MedidaPesoModel medida = new MedidaPesoModel();
        medida.setNombrePeso(request.getNombrePeso());
        medida.setAbreviaturaPeso(request.getAbreviaturaPeso());
        medida.setActivo(true);
        medida.setFechaCreacion(LocalDateTime.now());
        medida.setUsuarioCreacion(usuarioCreacion);

        MedidaPesoModel guardado = medidaPesoRepository.save(medida);
        return convertirAResponse(guardado);
    }

    public MedidaPesoResponse actualizar(Long id, MedidaPesoRequest request, String usuarioModificacion) {
        MedidaPesoModel medida = medidaPesoRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new IllegalArgumentException("Medida no encontrada"));

        medida.setNombrePeso(request.getNombrePeso());
        medida.setAbreviaturaPeso(request.getAbreviaturaPeso());
        medida.setFechaModificacion(LocalDateTime.now());
        medida.setUsuarioModificacion(usuarioModificacion);

        return convertirAResponse(medidaPesoRepository.save(medida));
    }

    public void eliminar(Long id) {
        MedidaPesoModel medida = medidaPesoRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new IllegalArgumentException("Medida no encontrada"));
        medida.setActivo(false);
        medidaPesoRepository.save(medida);
    }

    public List<MedidaPesoResponse> listarActivos() {
        return medidaPesoRepository.findByActivoTrue().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    private MedidaPesoResponse convertirAResponse(MedidaPesoModel m) {
        MedidaPesoResponse response = new MedidaPesoResponse();
        response.setId(m.getId());
        response.setNombrePeso(m.getNombrePeso());
        response.setAbreviaturaPeso(m.getAbreviaturaPeso());
        response.setActivo(m.isActivo());
        response.setFechaCreacion(m.getFechaCreacion().toLocalDate());
        response.setUsuarioCreacion(m.getUsuarioCreacion());
        response.setFechaModificacion(m.getFechaModificacion() != null ? m.getFechaModificacion().toLocalDate() : null);
        response.setUsuarioModificacion(m.getUsuarioModificacion());
        return response;
    }
}
