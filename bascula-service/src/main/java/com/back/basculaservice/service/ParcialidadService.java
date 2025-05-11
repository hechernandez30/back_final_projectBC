package com.back.basculaservice.service;

import com.back.basculaservice.dto.Request.ParcialidadRequest;
import com.back.basculaservice.dto.Response.ParcialidadResponse;
import com.back.basculaservice.model.ParcialidadModel;
import com.back.basculaservice.repository.ParcialidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParcialidadService {
    private final ParcialidadRepository parcialidadRepository;
    public ParcialidadResponse crear(ParcialidadRequest request, String usuario) {

        // Calcular diferencia de peso si no viene en el request
        Float pesoEnviado = request.getPesoEnviado();
        Float pesoBascula = request.getPesoBascula();
        Float diferenciaPeso = request.getDiferenciaPeso();

        if (diferenciaPeso == null && pesoEnviado != null && pesoBascula != null) {
            diferenciaPeso = pesoBascula - pesoEnviado;
        }

        // Validar si la diferencia excede el 5% del peso enviado
        if (pesoEnviado != null && diferenciaPeso != null) {
            float porcentajeDiferencia = Math.abs(diferenciaPeso) / pesoEnviado;
            if (porcentajeDiferencia > 0.05f) {
                throw new IllegalArgumentException("La diferencia de peso excede el 5% permitido.");
            }
        }

        // Crear parcialidad
        ParcialidadModel model = new ParcialidadModel();
        model.setSolicitudId(request.getSolicitudId());
        model.setPlacaTransporte(request.getPlacaTransporte());
        model.setNombreTransportista(request.getNombreTransportista());
        model.setCuiTransportista(request.getCuiTransportista());
        model.setMedidaPesoId(request.getMedidaPesoId());
        model.setFechaRecepcionParcialidad(request.getFechaRecepcionParcialidad());
        model.setPesoEnviado(request.getPesoEnviado());
        model.setPesoBascula(request.getPesoBascula());
        model.setDiferenciaPeso(request.getDiferenciaPeso());
        model.setFechaPesoBascula(request.getFechaPesoBascula());
        model.setActivo(true);
        model.setObservaciones(request.getObservaciones());
        model.setUsuarioCreacion(usuario);

        return toDto(parcialidadRepository.save(model));
    }

    //Metodo para actualizar un registro
    public ParcialidadResponse modificar(int id, ParcialidadRequest request, String usuario) {
        ParcialidadModel model = parcialidadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la parcialidad con ID: " + id));

        // Actualizar campos
        model.setPlacaTransporte(request.getPlacaTransporte());
        model.setNombreTransportista(request.getNombreTransportista());
        model.setCuiTransportista(request.getCuiTransportista());
        model.setMedidaPesoId(request.getMedidaPesoId());
        model.setFechaRecepcionParcialidad(request.getFechaRecepcionParcialidad());
        model.setPesoEnviado(request.getPesoEnviado());
        model.setPesoBascula(request.getPesoBascula());
        model.setDiferenciaPeso(request.getDiferenciaPeso());
        model.setFechaPesoBascula(request.getFechaPesoBascula());
        model.setObservaciones(request.getObservaciones());
        model.setUsuarioModificacion(usuario);

        return toDto(parcialidadRepository.save(model));
    }

    public List<ParcialidadResponse> listarActivos() {
        return parcialidadRepository.findByActivoTrue()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ParcialidadResponse> listarPorSolicitud(int solicitudId) {
        return parcialidadRepository.findBySolicitudIdAndActivoTrue(solicitudId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ParcialidadResponse buscarPorId(int id) {
        ParcialidadModel model = parcialidadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la parcialidad con ID: " + id));
        return toDto(model);
    }

    public void eliminarLogico(int id, String usuario) {
        ParcialidadModel model = parcialidadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la parcialidad con ID: " + id));
        model.setActivo(false);
        model.setUsuarioModificacion(usuario);
        parcialidadRepository.save(model);
    }

    private ParcialidadResponse toDto(ParcialidadModel model) {
        ParcialidadResponse dto = new ParcialidadResponse();
        BeanUtils.copyProperties(model, dto);
        return dto;
    }
}
