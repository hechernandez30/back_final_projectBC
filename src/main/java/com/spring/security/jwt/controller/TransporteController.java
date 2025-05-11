package com.spring.security.jwt.controller;

import com.spring.security.jwt.dto.TransporteRequest;
import com.spring.security.jwt.dto.TransporteResponse;
import com.spring.security.jwt.service.TransporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transportes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TransporteController {
    private final TransporteService transporteService;

    @GetMapping
    public ResponseEntity<List<TransporteResponse>> listarActivos() {
        return ResponseEntity.ok(transporteService.listarActivos());
    }

    @PostMapping
    public ResponseEntity<TransporteResponse> crear(@RequestBody TransporteRequest request,
                                                    @RequestHeader("X-Usuario") String usuario) {
        return ResponseEntity.ok(transporteService.crear(request, usuario));
    }

    @PutMapping("/{placa}")
    public ResponseEntity<TransporteResponse> modificar(@PathVariable String placa,
                                                        @RequestBody TransporteRequest request,
                                                        @RequestHeader("X-Usuario") String usuario) {
        return ResponseEntity.ok(transporteService.modificar(placa, request, usuario));
    }

    @DeleteMapping("/{placa}")
    public ResponseEntity<Void> eliminar(@PathVariable String placa,
                                               @RequestHeader("X-Usuario") String usuario) {
        transporteService.eliminar(placa, usuario);
        return ResponseEntity.noContent().build();
    }
}
