package com.back.agricultorservice.controller;

import com.back.agricultorservice.dto.TblAgricultorRequest;
import com.back.agricultorservice.dto.TblAgricultorResponse;
import com.back.agricultorservice.service.TblAgricultorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agricultores")
@RequiredArgsConstructor
public class TblAgricultorController {
    private final TblAgricultorService agricultorService;

    @PostMapping
    public ResponseEntity<TblAgricultorResponse> crear(@RequestBody TblAgricultorRequest request, @RequestHeader("X-Usuario") String usuarioCreacion) {
        return ResponseEntity.ok(agricultorService.crearAgricultor(request, usuarioCreacion));
    }

    @PutMapping("/actualizar/{nit}")
    public ResponseEntity<TblAgricultorResponse> actualizar(@PathVariable String nit, @RequestBody TblAgricultorRequest request, @RequestHeader("X-Usuario") String usuarioCreacion) {
        return ResponseEntity.ok(agricultorService.actualizarAgricultor(nit, request, usuarioCreacion));
    }

    @DeleteMapping("/eliminar/{nit}")
    public ResponseEntity<String> eliminar(@PathVariable String nit, @RequestHeader("X-Usuario") String usuarioCreacion) {
        agricultorService.eliminarLogicamente(nit, usuarioCreacion);
        return ResponseEntity.ok("Agricultor eliminado (lógicamente) correctamente");
    }

    @GetMapping("/{nit}")
    public ResponseEntity<TblAgricultorResponse> buscarPorNit(@PathVariable String nit) {
        return ResponseEntity.ok(agricultorService.buscarPorNit(nit));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<TblAgricultorResponse>> listarActivos() {
        return ResponseEntity.ok(agricultorService.listarActivos());
    }
}
