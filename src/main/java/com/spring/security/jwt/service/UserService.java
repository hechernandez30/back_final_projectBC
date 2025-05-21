package com.spring.security.jwt.service;

import com.spring.security.jwt.dto.UserRequestDto;
import com.spring.security.jwt.dto.UserResponseDto;
import com.spring.security.jwt.model.UserModel;
import com.spring.security.jwt.repository.IUserRepository;
import com.spring.security.jwt.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserJpaRepository repository;

    public UserResponseDto crear(UserRequestDto request, String usuario) {
        UserModel model = new UserModel();
        model.setNit(request.getNit());
        model.setNombre(request.getNombre());
        model.setUsuario(request.getUsuario());
        model.setContrasena(request.getContrasena());
        model.setRol(request.getRol());
        model.setObservaciones(request.getObservaciones());
        model.setActivo(true);
        model.setFechaCreacion(LocalDateTime.now());
        model.setUsuarioCreacion(usuario);
        return toResponse(repository.save(model));
    }

    public List<UserResponseDto> listarActivos() {
        return repository.findByActivoTrue().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public UserResponseDto buscarPorNit(String nit) {
        UserModel model = repository.findByNit(nit)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado por NIT: " + nit));
        return toResponse(model);
    }

    public UserResponseDto actualizar(Integer id, UserRequestDto request, String usuarioMod) {
        UserModel model = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        model.setNombre(request.getNombre());
        model.setUsuario(request.getUsuario());
        model.setContrasena(request.getContrasena());
        model.setRol(request.getRol());
        model.setObservaciones(request.getObservaciones());
        model.setFechaModificacion(LocalDateTime.now());
        model.setUsuarioModificacion(usuarioMod);
        return toResponse(repository.save(model));
    }

    public void eliminarLogico(Integer id, String usuario) {
        UserModel model = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        model.setActivo(false);
        model.setFechaModificacion(LocalDateTime.now());
        model.setUsuarioModificacion(usuario);
        repository.save(model);
    }

    private UserResponseDto toResponse(UserModel model) {
        UserResponseDto dto = new UserResponseDto();
        dto.setUser_id(model.getUser_id());
        dto.setNit(model.getNit());
        dto.setNombre(model.getNombre());
        dto.setUsuario(model.getUsuario());
        dto.setRol(model.getRol());
        dto.setObservaciones(model.getObservaciones());
        dto.setActivo(model.isActivo());
        dto.setFechaCreacion(model.getFechaCreacion());
        dto.setUsuarioCreacion(model.getUsuarioCreacion());
        dto.setFechaModificacion(model.getFechaModificacion());
        dto.setUsuarioModificacion(model.getUsuarioModificacion());
        return dto;
    }
}
