package com.back.basculaservice.controller;

import com.back.basculaservice.dto.Request.BoletaRequest;
import com.back.basculaservice.dto.Response.BoletaResponse;
import com.back.basculaservice.service.BoletaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boletas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class BoletaController {
    private final BoletaService boletaService;

    @PostMapping("/crear")
    public ResponseEntity<BoletaResponse> crear(@RequestBody BoletaRequest request) {
        String usuario = "admin"; // puedes reemplazar por usuario autenticado
        return ResponseEntity.ok(boletaService.crear(request, usuario));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<BoletaResponse> actualizar(@PathVariable int id, @RequestBody BoletaRequest request) {
        String usuario = "admin";
        return ResponseEntity.ok(boletaService.actualizar(id, request, usuario));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        String usuario = "admin";
        boletaService.eliminar(id, usuario);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listar")
    public ResponseEntity<List<BoletaResponse>> listar() {
        return ResponseEntity.ok(boletaService.listar());
    }
}
