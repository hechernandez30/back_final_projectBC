package com.spring.security.jwt.controller;

import com.spring.security.jwt.dto.UserRequestDto;
import com.spring.security.jwt.dto.UserResponseDto;
import com.spring.security.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    public ResponseEntity<UserResponseDto> crear(@RequestBody UserRequestDto request,
                                                 @RequestHeader("X-Usuario") String usuario) {
        return ResponseEntity.ok(service.crear(request, usuario));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<UserResponseDto>> listarActivos() {
        return ResponseEntity.ok(service.listarActivos());
    }

    @GetMapping("/buscar/{nit}")
    public ResponseEntity<UserResponseDto> buscarPorNit(@PathVariable String nit) {
        return ResponseEntity.ok(service.buscarPorNit(nit));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<UserResponseDto> actualizar(@PathVariable Integer id,
                                                      @RequestBody UserRequestDto request,
                                                      @RequestHeader("X-Usuario") String usuarioMod) {
        return ResponseEntity.ok(service.actualizar(id, request, usuarioMod));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id,
                                         @RequestHeader("X-Usuario") String usuario) {
        service.eliminarLogico(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
