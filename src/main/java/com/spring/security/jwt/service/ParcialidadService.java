package com.spring.security.jwt.service;

import com.spring.security.jwt.dto.ParcialidadRequestDto;
import com.spring.security.jwt.dto.ParcialidadResponseDto;
import com.spring.security.jwt.model.CuentaModel;
import com.spring.security.jwt.model.ParcialidadModel;
import com.spring.security.jwt.model.TransporteModel;
import com.spring.security.jwt.model.TransportistaAutorizadoModel;
import com.spring.security.jwt.repository.CuentaRepository;
import com.spring.security.jwt.repository.ParcialidadRepository;
import com.spring.security.jwt.repository.TransporteRepository;
import com.spring.security.jwt.repository.TransportistaAutorizadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParcialidadService {
    private final ParcialidadRepository parcialidadRepository;
    private final CuentaRepository cuentaRepository;
    private final TransporteRepository transporteRepository;
    private final TransportistaAutorizadoRepository transportistaAutorizadoRepository;

    public ParcialidadResponseDto crear(ParcialidadRequestDto request, String usuario) {
        // Validar existencia y estado activo de la solicitud
        CuentaModel cuenta = cuentaRepository.findById(request.getSolicitudId())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró una solicitud (cuenta) con ID: " + request.getSolicitudId()));

        // Validar transporte activo
        TransporteModel transporte = transporteRepository.findById(request.getPlacaTransporte())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró transporte con placa: " + request.getPlacaTransporte()));
        if (!transporte.isActivo()) {
            throw new IllegalArgumentException("El transporte con placa " + request.getPlacaTransporte() + " no está activo.");
        }

        // Validar transportista activo
        TransportistaAutorizadoModel transportista = transportistaAutorizadoRepository.findByCuiTransportista(request.getCuiTransportista())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró transportista con CUI: " + request.getCuiTransportista()));
        if (!transportista.isActivo()) {
            throw new IllegalArgumentException("El transportista con CUI " + request.getCuiTransportista() + " no está activo.");
        }

        // Verificar si ya se alcanzó el número de parcialidades permitidas
        long parcialidadesRegistradas = parcialidadRepository
                .findBySolicitudIdAndActivoTrue(request.getSolicitudId()).size();
        if (parcialidadesRegistradas >= cuenta.getCantParcialidades()) {
            throw new IllegalArgumentException("Ya se han registrado todas las parcialidades permitidas para esta solicitud.");
        }

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
    public ParcialidadResponseDto modificar(int id, ParcialidadRequestDto request, String usuario) {
        ParcialidadModel model = parcialidadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la parcialidad con ID: " + id));

        // Validar transporte
        TransporteModel transporte = transporteRepository.findById(request.getPlacaTransporte())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró transporte con placa: " + request.getPlacaTransporte()));
        if (!transporte.isActivo()) {
            throw new IllegalArgumentException("El transporte con placa " + request.getPlacaTransporte() + " no está activo.");
        }

        // Validar transportista
        TransportistaAutorizadoModel transportista = transportistaAutorizadoRepository.findByCuiTransportista(request.getCuiTransportista())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró transportista con CUI: " + request.getCuiTransportista()));
        if (!transportista.isActivo()) {
            throw new IllegalArgumentException("El transportista con CUI " + request.getCuiTransportista() + " no está activo.");
        }

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

    public List<ParcialidadResponseDto> listarActivos() {
        return parcialidadRepository.findByActivoTrue()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ParcialidadResponseDto> listarPorSolicitud(int solicitudId) {
        return parcialidadRepository.findBySolicitudIdAndActivoTrue(solicitudId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ParcialidadResponseDto buscarPorId(int id) {
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

    private ParcialidadResponseDto toDto(ParcialidadModel model) {
        ParcialidadResponseDto dto = new ParcialidadResponseDto();
        BeanUtils.copyProperties(model, dto);
        return dto;
    }
}
