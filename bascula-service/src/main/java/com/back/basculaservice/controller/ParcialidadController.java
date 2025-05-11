package com.back.basculaservice.controller;

import com.back.basculaservice.dto.Request.ParcialidadRequest;
import com.back.basculaservice.dto.Response.ParcialidadResponse;
import com.back.basculaservice.service.ParcialidadService;
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
    public ResponseEntity<ParcialidadResponse> crear(
            @RequestBody ParcialidadRequest request,
            @RequestHeader("X-Usuario") String usuario) {
        return ResponseEntity.ok(parcialidadService.crear(request, usuario));
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<ParcialidadResponse> modificar(
            @PathVariable int id,
            @RequestBody ParcialidadRequest request,
            @RequestHeader("X-Usuario") String usuario) {
        return ResponseEntity.ok(parcialidadService.modificar(id, request, usuario));
    }


    @GetMapping("/listar")
    public ResponseEntity<List<ParcialidadResponse>> listarActivos() {
        return ResponseEntity.ok(parcialidadService.listarActivos());
    }

    @GetMapping("/solicitud/{solicitudId}")
    public ResponseEntity<List<ParcialidadResponse>> listarPorSolicitud(@PathVariable int solicitudId) {
        return ResponseEntity.ok(parcialidadService.listarPorSolicitud(solicitudId));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<ParcialidadResponse> buscarPorId(@PathVariable int id) {
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
