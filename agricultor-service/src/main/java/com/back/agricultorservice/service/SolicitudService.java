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

        // 3. Construir la solicitud LOCAL, pero NO guardar aún
        SolicitudModel solicitud = new SolicitudModel();
        solicitud.setNumeroCuenta(0); // Temporal
        solicitud.setNitAgricultor(request.getNitAgricultor());
        solicitud.setMedidaPeso(medidaPeso);
        solicitud.setPesoAcordado(request.getPesoAcordado());
        solicitud.setCantParcialidades(request.getCantParcialidades());
        solicitud.setPesoCadaParcialidad(request.getPesoCadaParcialidad());
        solicitud.setObservaciones(request.getObservaciones());
        solicitud.setEstado("Cuenta Creada"); // Default
        solicitud.setFechaCreacion(LocalDateTime.now());
        solicitud.setUsuarioCreacion(usuarioCreacion);

        // 4. Primero intentamos enviar al microservicio de beneficio:
        try {
            CuentaRequestDto cuenta = SolicitudMapper.convertirASolicitudBeneficio(request);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Usuario", usuarioCreacion);

            HttpEntity<CuentaRequestDto> entity = new HttpEntity<>(cuenta, headers);

            ResponseEntity<CuentaResponseDto> response = restTemplate.exchange(
                    "http://localhost:3000/api/cuentas",
                    HttpMethod.POST,
                    entity,
                    CuentaResponseDto.class);

            // 5. Si la llamada fue exitosa:
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                int numeroGenerado = response.getBody().getNumeroCuenta();
                solicitud.setNumeroCuenta(numeroGenerado);

                // Ahora sí → guardar en Agricultor
                SolicitudModel solicitudGuardada = solicitudRepository.save(solicitud);

                System.out.println("✅ Cuenta creada y número sincronizado desde Beneficio.");

                return convertirASolicitudResponse(solicitudGuardada);
            } else {
                throw new RuntimeException("Error al crear cuenta en el módulo Beneficio.");
            }

        } catch (Exception e) {
            // Si falla → NO guardar nada en Agricultor
            throw new RuntimeException("❌ Error al enviar cuenta al microservicio beneficio: " + e.getMessage());
        }
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

    public SolicitudResponse actualizarEstado(int id, String nuevoEstado, String usuarioModificacion) {
        SolicitudModel solicitud = solicitudRepository.findById((long) id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada con ID: " + id));

        solicitud.setEstado(nuevoEstado);
        solicitud.setUsuarioModificacion(usuarioModificacion);
        solicitud.setFechaModificacion(LocalDateTime.now());

        SolicitudModel actualizada = solicitudRepository.save(solicitud);

        return convertirASolicitudResponse(actualizada);
    }


}
