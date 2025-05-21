package com.back.agricultorservice.controller;

import com.back.agricultorservice.dto.SolicitudResponse;
import com.back.agricultorservice.dto.TransportistaRequest;
import com.back.agricultorservice.dto.TransportistaResponse;
import com.back.agricultorservice.service.TransportistaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transportistas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransportistaController {
    private final TransportistaService transportistaService;

    @PostMapping//Crear transportista
    public ResponseEntity<TransportistaResponse> crearTransportista(@RequestBody TransportistaRequest request, @RequestHeader("X-Usuario") String usuarioCreacion) {
        TransportistaResponse response = transportistaService.crearTransportista(request, usuarioCreacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/piloto/{cui}")
    public TransportistaResponse obtenerTransportistaPorCui(@PathVariable String cui){
        return transportistaService.obtenerTransportistaPorCui(cui);
    }
    @PutMapping("/actualizar/{cui}")//Actualizar un transportista por Cui
    public ResponseEntity<TransportistaResponse> actualizarTransportista(
            @PathVariable String cui,
            @RequestBody TransportistaRequest request) {

        TransportistaResponse actualizado = transportistaService.actualizarTransportista(cui, request);
        return ResponseEntity.ok(actualizado);
    }
    @DeleteMapping("/eliminar/{cui}")//Eliminar un transportista por CUI
    public ResponseEntity<String> eliminarTransportista(@PathVariable String cui) {
        transportistaService.eliminarTransportista(cui);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/listar")//Obtener todos los trasnportistas activos
    public ResponseEntity<List<TransportistaResponse>> obtenerTransportistasActivos() {
        List<TransportistaResponse> activos = transportistaService.obtenerTransportistasActivos();
        return ResponseEntity.ok(activos);
    }

}
