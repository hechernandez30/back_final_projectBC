package com.back.agricultorservice.service;

import com.back.agricultorservice.dto.SolicitudRequest;
import com.back.agricultorservice.dto.SolicitudResponse;
import com.back.agricultorservice.model.*;
import com.back.agricultorservice.repository.*;
import com.back.agricultorservice.utils.SolicitudMapper;
import com.spring.security.jwt.dto.CuentaRequestDto;
import com.spring.security.jwt.dto.CuentaResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitudService {
    private final SolicitudRepository solicitudRepository;
    private final RestTemplate restTemplate;
    private final TransporteRepository transporteRepository;
    private final TransportistaRepository transportistaRepository;
    private final MedidaPesoRepository medidaPesoRepository;

    @Transactional
    public SolicitudResponse crearSolicitud(SolicitudRequest request, String usuarioCreacion) {
        // 1. Validar medida de peso
        MedidaPesoModel medidaPeso = medidaPesoRepository.findById(request.getMedidaPesoId())
                .orElseThrow(() -> new IllegalArgumentException("Medida de peso no encontrada con ID: " + request.getMedidaPesoId()));

        // 2. Validar coherencia de pesos
        float pesoCalculado = request.getCantParcialidades() * request.getPesoCadaParcialidad();
        if (!request.getPesoAcordado().equals(pesoCalculado)) {
            throw new IllegalArgumentException("El peso acordado no coincide con el cálculo de parcialidades");
        }

        // 3. Crear y guardar la solicitud localmente con numeroCuenta temporal (0)
        SolicitudModel solicitud = new SolicitudModel();
        solicitud.setNumeroCuenta(0); // Temporal
        solicitud.setNitAgricultor(request.getNitAgricultor());
        solicitud.setMedidaPeso(medidaPeso);
        solicitud.setPesoAcordado(request.getPesoAcordado());
        solicitud.setCantParcialidades(request.getCantParcialidades());
        solicitud.setPesoCadaParcialidad(request.getPesoCadaParcialidad());
        solicitud.setObservaciones(request.getObservaciones());
        solicitud.setEstado("Cuenta Creada"); // Ya que lo decidiste como default
        solicitud.setFechaCreacion(LocalDateTime.now());
        solicitud.setUsuarioCreacion(usuarioCreacion);

        SolicitudModel solicitudGuardada = solicitudRepository.save(solicitud);

        // 4. Enviar al microservicio de beneficio y recibir el número de cuenta generado
        try {
            CuentaRequestDto cuenta = SolicitudMapper.convertirASolicitudBeneficio(request);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Usuario", usuarioCreacion);

            HttpEntity<CuentaRequestDto> entity = new HttpEntity<>(cuenta, headers);
            //ResponseEntity<CuentaResponseDto> response = restTemplate.postForEntity(
            ResponseEntity<CuentaResponseDto> response = restTemplate.exchange(
                    "http://localhost:3000/api/cuentas",
                    HttpMethod.POST,
                    entity,
                    CuentaResponseDto.class);

            // 5. Sincronizar número de cuenta si fue retornado
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                int numeroGenerado = response.getBody().getNumeroCuenta();
                solicitudGuardada.setNumeroCuenta(numeroGenerado);
                //solicitudRepository.save(solicitudGuardada); // Guardar actualización
            }

            System.out.println("✅ Cuenta creada y número sincronizado desde Beneficio.");
        } catch (Exception e) {
            System.out.println("❌ Error al enviar cuenta al microservicio beneficio: " + e.getMessage());
        }

        return convertirASolicitudResponse(solicitudGuardada);
    }


    // Metodo para obtener todas las solicitudes de un agricultor
    public List<SolicitudResponse> listarSolicitudesPorAgricultor(String nitAgricultor) {
        return solicitudRepository.findByNitAgricultor(nitAgricultor)
                .stream()
                .map(this::convertirASolicitudResponse)
                .collect(Collectors.toList());
    }

    // Metodo para obtener una solicitud por ID
    public SolicitudResponse obtenerSolicitudPorId(Long id) {
        SolicitudModel solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada con ID: " + id));
        return convertirASolicitudResponse(solicitud);
    }
    private SolicitudResponse convertirASolicitudResponse(SolicitudModel solicitud) {
        SolicitudResponse response = new SolicitudResponse();
        response.setSolicitudId(solicitud.getId());
        response.setNumeroCuenta(solicitud.getNumeroCuenta());
        response.setNitAgricultor(solicitud.getNitAgricultor());
        response.setPesoAcordado(solicitud.getPesoAcordado());
        response.setCantParcialidades(solicitud.getCantParcialidades());
        response.setPesoCadaParcialidad(solicitud.getPesoCadaParcialidad());
        response.setMedidaPeso(solicitud.getMedidaPeso().getNombrePeso() + " (" + solicitud.getMedidaPeso().getAbreviaturaPeso() + ")");
        response.setObservaciones(solicitud.getObservaciones());
        response.setEstado(solicitud.getEstado());
        response.setFechaCreacion(solicitud.getFechaCreacion());
        return response;
    }
    //Metodo para listar todas las solicitudes
    public List<SolicitudResponse> obtenerTodasLasSolicitudes() {
        List<SolicitudModel> solicitudes = solicitudRepository.findAll();
        return solicitudes.stream().map(this::convertirASolicitudResponse).toList();
    }

    public void actualizarEstadoDesdeBeneficio(int numeroCuenta, String nuevoEstado) {
        SolicitudModel solicitud = solicitudRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new NoSuchElementException("No se encontró solicitud con ese número de cuenta"));

        solicitud.setEstado(nuevoEstado);
        solicitud.setFechaModificacion(LocalDateTime.now());
        solicitud.setUsuarioModificacion("SINCRONIZADO");
        solicitudRepository.save(solicitud);
    }

}
