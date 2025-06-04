package com.back.agricultorservice.controller;

import com.back.agricultorservice.dto.SolicitudRequest;
import com.back.agricultorservice.dto.SolicitudResponse;
import com.back.agricultorservice.model.SolicitudModel;
import com.back.agricultorservice.repository.SolicitudRepository;
import com.back.agricultorservice.service.SolicitudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {
   private final SolicitudService solicitudService;
   private final SolicitudRepository solicitudRepository;

    @PostMapping
    public ResponseEntity<SolicitudResponse> crearSolicitud(@RequestBody SolicitudRequest request, @RequestHeader("X-Usuario") String usuarioCreacion) {
        SolicitudResponse response = solicitudService.crearSolicitud(request, usuarioCreacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/agricultor/{nit}")
    public List<SolicitudResponse> listarPorAgricultor(@PathVariable String nit){
        return solicitudService.listarSolicitudesPorAgricultor(nit);
    }
    @PutMapping("/actualizar-estado/{numeroCuenta}")
    public ResponseEntity<Void> actualizarEstado(
            @PathVariable int numeroCuenta,
            @RequestBody String nuevoEstado) {

        SolicitudModel solicitud = solicitudRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new NoSuchElementException("Solicitud no encontrada para cuenta " + numeroCuenta));

        solicitud.setEstado(nuevoEstado);
        solicitud.setFechaModificacion(LocalDateTime.now());
        solicitudRepository.save(solicitud);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public SolicitudResponse obtenerPorId(@PathVariable Long id){
        return solicitudService.obtenerSolicitudPorId(id);
    }
    @GetMapping("/listar")
    public ResponseEntity<List<SolicitudResponse>> listarSolicitudes() {
        List<SolicitudResponse> solicitudes = solicitudService.obtenerTodasLasSolicitudes();
        return ResponseEntity.ok(solicitudes);
    }
}
