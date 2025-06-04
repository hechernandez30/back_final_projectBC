package com.back.agricultorservice.service;

import com.back.agricultorservice.dto.TransporteRequest;
import com.back.agricultorservice.dto.TransporteResponse;
import com.back.agricultorservice.model.TransporteModel;
import com.back.agricultorservice.repository.TransporteRepository;
import com.spring.security.jwt.model.UserModel;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransporteService {
    private final TransporteRepository transporteRepository;
    @Autowired
    private RestTemplate restTemplate;

    public TransporteResponse crearTransporte(TransporteRequest request, String usuarioCreacion) {
        // Validación previa
        Optional<TransporteModel> existente = transporteRepository.findById(request.getPlacaTransporte());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un transporte con esa placa.");
        }

        // Guardar localmente
        TransporteModel transporte = new TransporteModel();
        transporte.setPlacaTransporte(request.getPlacaTransporte());
        transporte.setNitAgricultor(request.getNitAgricultor());
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

        // Enviar al microservicio Beneficio
        try {
            com.spring.security.jwt.dto.TransporteRequest requestBeneficio = new com.spring.security.jwt.dto.TransporteRequest();
            requestBeneficio.setPlacaTransporte(transporte.getPlacaTransporte());

            UserModel usuarioMock = new UserModel(); // Solo necesita el NIT
            usuarioMock.setNit(transporte.getNitAgricultor());
            requestBeneficio.setNitAgricultor(usuarioMock);

            requestBeneficio.setTipoPlaca(transporte.getTipoPlaca());
            requestBeneficio.setMarca(transporte.getMarca());
            requestBeneficio.setColor(transporte.getColor());
            requestBeneficio.setLinea(transporte.getLinea());
            requestBeneficio.setModelo(transporte.getModelo());
            requestBeneficio.setObservaciones(transporte.getObservaciones());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Usuario", usuarioCreacion);

            HttpEntity<com.spring.security.jwt.dto.TransporteRequest> entity = new HttpEntity<>(requestBeneficio, headers);
            restTemplate.postForEntity("http://localhost:3000/api/transportes", entity, Void.class);

            System.out.println("✅ Transporte también creado en el microservicio Beneficio.");
        } catch (Exception e) {
            System.err.println("❌ Error al sincronizar con Beneficio: " + e.getMessage());
        }

        return convertirAResponse(guardado);
    }

    public TransporteResponse actualizarTransporte(String placa, TransporteRequest request, String usuarioModificacion) {
        TransporteModel transporte = transporteRepository.findById(placa)
                .orElseThrow(() -> new IllegalArgumentException("Transporte no encontrado"));
        if (!transporte.isActivo()) {
            throw new IllegalArgumentException("No se puede modificar un trasnporte inactivo.");
        }

        transporte.setNitAgricultor(request.getNitAgricultor());
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
        response.setNitAgricultor(t.getNitAgricultor());
        response.setTipoPlaca(t.getTipoPlaca());
        response.setMarca(t.getMarca());
        response.setColor(t.getColor());
        response.setLinea(t.getLinea());
        response.setModelo(t.getModelo());
        response.setActivo(t.isActivo());
//        response.setDisponible(t.isDisponible());
//        response.setPesajeAsociado(t.getPesajeAsociado());
        response.setObservaciones(t.getObservaciones());
        response.setFechaCreacion(t.getFechaCreacion().toLocalDate());
        response.setUsuarioCreacion(t.getUsuarioCreacion());
        response.setFechaModificacion(t.getFechaModificacion() != null ? t.getFechaModificacion().toLocalDate() : null);
        response.setUsuarioModificacion(t.getUsuarioModificacion());
        return response;
    }
}
