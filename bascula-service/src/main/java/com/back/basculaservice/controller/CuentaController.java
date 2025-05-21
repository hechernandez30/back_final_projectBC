package com.back.basculaservice.controller;

import com.back.basculaservice.dto.Request.CuentaRequest;
import com.back.basculaservice.dto.Response.CuentaResponse;
import com.back.basculaservice.service.CuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CuentaController {
    private final CuentaService cuentaService;

    @PostMapping("/crear")
    public ResponseEntity<CuentaResponse> crear(
            @RequestBody CuentaRequest request,
            @RequestHeader("X-Usuario") String usuarioCreacion) {
        return ResponseEntity.ok(cuentaService.crear(request, usuarioCreacion));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<CuentaResponse> actualizar(
            @PathVariable int id,
            @RequestBody CuentaRequest request,
            @RequestHeader("X-Usuario") String usuarioModificacion) {
        return ResponseEntity.ok(cuentaService.actualizar(id, request, usuarioModificacion));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable int id,
            @RequestHeader("X-Usuario") String usuarioEliminacion) {
        cuentaService.eliminar(id, usuarioEliminacion);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listar")
    public ResponseEntity<List<CuentaResponse>> listar() {
        return ResponseEntity.ok(cuentaService.listarActivos());
    }
}
