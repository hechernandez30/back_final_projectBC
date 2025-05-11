package com.back.agricultorservice.controller;

import com.back.agricultorservice.dto.ParcialidadRequest;
import com.back.agricultorservice.dto.ParcialidadResponse;
import com.back.agricultorservice.service.ParcialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parcialidades")
@RequiredArgsConstructor
public class ParcialidadController {
    private final ParcialidadService parcialidadService;

    @PostMapping
    public ResponseEntity<ParcialidadResponse> crearParcialidad(@RequestBody ParcialidadRequest request, @RequestHeader("X-Usuario") String usuarioCreacion) {
        return ResponseEntity.ok(parcialidadService.crearParcialidad(request, usuarioCreacion));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarParcialidad(@PathVariable int id, @RequestBody ParcialidadRequest request, @RequestHeader("X-Usuario") String usuarioModificacion) {
        parcialidadService.actualizarParcialidad(id, request, usuarioModificacion);
        return ResponseEntity.ok("Parcialidad actualizada correctamente");
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarParcialidad(@PathVariable int id, @RequestHeader("X-Usuario") String usuarioEliminacion) {
        parcialidadService.eliminarParcialidad(id, usuarioEliminacion);
        return ResponseEntity.ok("Parcialidad eliminada lógicamente");
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ParcialidadResponse>> obtenerTodas() {
        return ResponseEntity.ok(parcialidadService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParcialidadResponse> obtenerPorId(@PathVariable int id) {
        return ResponseEntity.ok(parcialidadService.obtenerPorId(id));
    }

    @GetMapping("/solicitud/{solicitudId}")
    public ResponseEntity<List<ParcialidadResponse>> listarPorSolicitud(@PathVariable int solicitudId) {
        return ResponseEntity.ok(parcialidadService.listarPorSolicitud(solicitudId));
    }

    @GetMapping("/solicitud/{solicitudId}/completa")
    public ResponseEntity<Boolean> verificarCompletas(@PathVariable int solicitudId) {
        return ResponseEntity.ok(parcialidadService.verificarParcialidadesCompletadas(solicitudId));
    }
}
