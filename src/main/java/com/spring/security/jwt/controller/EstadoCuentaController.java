package com.spring.security.jwt.controller;

import com.spring.security.jwt.dto.EstadoCuentaRequestDto;
import com.spring.security.jwt.dto.EstadoCuentaResponseDto;
import com.spring.security.jwt.service.EstadoCuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/estados")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class EstadoCuentaController {
    private final EstadoCuentaService estadoCuentaService;

    @PostMapping
    public ResponseEntity<EstadoCuentaResponseDto> crearEstadoCuenta(@RequestBody EstadoCuentaRequestDto request, @RequestHeader("X-Usuario") String usuarioCreacion) {
        return ResponseEntity.ok(estadoCuentaService.crearEstadoCuenta(request, usuarioCreacion));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EstadoCuentaResponseDto> actualizarEstadoCuenta(
            @PathVariable int id,
            @RequestBody EstadoCuentaRequestDto request,
            @RequestHeader("X-Usuario") String usuarioModificacion) {
        return ResponseEntity.ok(estadoCuentaService.actualizarEstadoCuenta(id, request, usuarioModificacion));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable int id,
            @RequestHeader("X-Usuario") String usuarioEliminacion) {
        estadoCuentaService.eliminar(id, usuarioEliminacion);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listar")
    public ResponseEntity<List<EstadoCuentaResponseDto>> listar() {
        return ResponseEntity.ok(estadoCuentaService.listarActivos());
    }

}
