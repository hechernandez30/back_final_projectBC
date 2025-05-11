package com.back.basculaservice.controller;

import com.back.basculaservice.dto.Request.MedidaPesoRequest;
import com.back.basculaservice.dto.Response.MedidaPesoResponse;
import com.back.basculaservice.service.MedidaPesoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medidas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MedidaPesoController {
    private final MedidaPesoService medidaPesoService;

    @PostMapping
    public ResponseEntity<MedidaPesoResponse> crearMedidaPeso(@RequestBody MedidaPesoRequest request, @RequestHeader("X-Usuario") String usuarioCreacion) {
        return ResponseEntity.ok(medidaPesoService.crearMedidaPeso(request, usuarioCreacion));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<MedidaPesoResponse> actualizar(@PathVariable Long id,
                                                         @RequestBody MedidaPesoRequest request,
                                                         @RequestHeader("X-Usuario") String usuarioModificacion) {
        return ResponseEntity.ok(medidaPesoService.actualizar(id, request, usuarioModificacion));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        medidaPesoService.eliminar(id);
        return ResponseEntity.ok("Registro eliminado (lógicamente) con éxito");
    }

    @GetMapping("/listar")
    public ResponseEntity<List<MedidaPesoResponse>> listarActivos() {
        return ResponseEntity.ok(medidaPesoService.listarActivos());
    }
}
