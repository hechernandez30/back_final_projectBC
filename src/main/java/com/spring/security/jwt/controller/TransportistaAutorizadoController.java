package com.spring.security.jwt.controller;

import com.spring.security.jwt.dto.TransportistaAutorizadoRequestDto;
import com.spring.security.jwt.dto.TransportistaAutorizadoResponseDto;
import com.spring.security.jwt.repository.TransportistaAutorizadoRepository;
import com.spring.security.jwt.service.TransportistaAutorizadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transportistas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TransportistaAutorizadoController {
    private final TransportistaAutorizadoService service;

    @PostMapping("/crear")
    public ResponseEntity<TransportistaAutorizadoResponseDto> crear(
            @RequestBody TransportistaAutorizadoRequestDto request,
            @RequestHeader("X-Usuario") String usuario) {
        return ResponseEntity.ok(service.crear(request, usuario));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<TransportistaAutorizadoResponseDto>> listarActivos() {
        return ResponseEntity.ok(service.listarActivos());
    }

    @GetMapping("/buscar/{cui}")
    public ResponseEntity<TransportistaAutorizadoResponseDto> buscarPorCui(@PathVariable String cui) {
        return ResponseEntity.ok(service.buscarPorCui(cui));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<TransportistaAutorizadoResponseDto> modificar(
            @PathVariable int id,
            @RequestBody TransportistaAutorizadoRequestDto request,
            @RequestHeader("X-Usuario") String usuario) {
        return ResponseEntity.ok(service.modificar(id, request, usuario));
    }

    @GetMapping("/buscar-id/{id}")
    public ResponseEntity<TransportistaAutorizadoResponseDto> buscarPorId(@PathVariable int id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarLogico(
            @PathVariable int id,
            @RequestHeader("X-Usuario") String usuario) {
        service.eliminarLogico(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
