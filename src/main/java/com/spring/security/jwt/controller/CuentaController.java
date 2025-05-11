package com.spring.security.jwt.controller;

import com.spring.security.jwt.dto.CuentaRequestDto;
import com.spring.security.jwt.dto.CuentaResponseDto;
import com.spring.security.jwt.service.CuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CuentaController {
    private final CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<CuentaResponseDto> crear(
            @RequestBody CuentaRequestDto request,
            @RequestHeader("X-Usuario") String usuarioCreacion) {
        return ResponseEntity.ok(cuentaService.crearCuenta(request, usuarioCreacion));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<CuentaResponseDto>> listar() {
        return ResponseEntity.ok(cuentaService.listarCuentas());
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<CuentaResponseDto> actualizar(
            @PathVariable int id,
            @RequestBody CuentaRequestDto request,
            @RequestHeader("X-Usuario") String usuarioModificacion) {
        return ResponseEntity.ok(cuentaService.actualizarCuenta(id, request, usuarioModificacion));
    }
}
