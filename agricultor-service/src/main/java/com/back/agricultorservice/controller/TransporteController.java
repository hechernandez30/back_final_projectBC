package com.back.agricultorservice.controller;

import com.back.agricultorservice.dto.TransporteRequest;
import com.back.agricultorservice.dto.TransporteResponse;
import com.back.agricultorservice.service.TransporteService;
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

    @PostMapping
    public ResponseEntity<TransporteResponse> crearSolicitud(@RequestBody TransporteRequest request, @RequestHeader("X-Usuario") String usuarioCreacion) {
        return ResponseEntity.ok(transporteService.crearTransporte(request, usuarioCreacion));
    }

    @PutMapping("/actualizar/{placa}")
    public ResponseEntity<TransporteResponse> actualizarTransporte(@PathVariable String placa,
                                                         @RequestBody TransporteRequest request,
                                                         @RequestHeader("X-Usuario") String usuarioModificacion) {
        return ResponseEntity.ok(transporteService.actualizarTransporte(placa, request, usuarioModificacion));
    }

    @DeleteMapping("/eliminar/{placa}")
    public ResponseEntity<String> eliminarTransporte(@PathVariable String placa) {
        transporteService.eliminarTransporte(placa);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listar")
    public ResponseEntity<List<TransporteResponse>> listarActivos() {
        return ResponseEntity.ok(transporteService.listarTransportesActivos());
    }
}
