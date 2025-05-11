package com.spring.security.jwt.controller;

import com.spring.security.jwt.dto.ParcialidadRequestDto;
import com.spring.security.jwt.dto.ParcialidadResponseDto;
import com.spring.security.jwt.service.ParcialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parcialidades")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ParcialidadController {
    private final ParcialidadService parcialidadService;

    @PostMapping("/crear")
    public ResponseEntity<ParcialidadResponseDto> crear(
            @RequestBody ParcialidadRequestDto request,
            @RequestHeader("X-Usuario") String usuario) {
        return ResponseEntity.ok(parcialidadService.crear(request, usuario));
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<ParcialidadResponseDto> modificar(
            @PathVariable int id,
            @RequestBody ParcialidadRequestDto request,
            @RequestHeader("X-Usuario") String usuario) {
        return ResponseEntity.ok(parcialidadService.modificar(id, request, usuario));
    }


    @GetMapping("/listar")
    public ResponseEntity<List<ParcialidadResponseDto>> listarActivos() {
        return ResponseEntity.ok(parcialidadService.listarActivos());
    }

    @GetMapping("/solicitud/{solicitudId}")
    public ResponseEntity<List<ParcialidadResponseDto>> listarPorSolicitud(@PathVariable int solicitudId) {
        return ResponseEntity.ok(parcialidadService.listarPorSolicitud(solicitudId));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<ParcialidadResponseDto> buscarPorId(@PathVariable int id) {
        return ResponseEntity.ok(parcialidadService.buscarPorId(id));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarLogico(
            @PathVariable int id,
            @RequestHeader("X-Usuario") String usuario) {
        parcialidadService.eliminarLogico(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
