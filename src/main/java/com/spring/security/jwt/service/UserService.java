package com.spring.security.jwt.service;

import com.spring.security.jwt.dto.UserRequestDto;
import com.spring.security.jwt.dto.UserResponseDto;
import com.spring.security.jwt.model.UserModel;
import com.spring.security.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
        return toResponse(userRepository.save(model));
    }

    public List<UserResponseDto> listarActivos() {
        return userRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponseDto buscarPorNit(String nit) {
        UserModel model = userRepository.findByNit(nit)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado por NIT: " + nit));
        return toResponse(model);
    }

    public UserResponseDto actualizar(Integer id, UserRequestDto request, String usuarioMod) {
        UserModel model = userRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        model.setNombre(request.getNombre());
        model.setUsuario(request.getUsuario());
        model.setContrasena(request.getContrasena());
        model.setRol(request.getRol());
        model.setObservaciones(request.getObservaciones());
        model.setFechaModificacion(LocalDateTime.now());
        model.setUsuarioModificacion(usuarioMod);
        return toResponse(userRepository.save(model));
    }

    public void eliminarLogico(Integer id, String usuario) {
        UserModel model = userRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        model.setActivo(false);
        model.setFechaModificacion(LocalDateTime.now());
        model.setUsuarioModificacion(usuario);
        userRepository.save(model);
    }

    public Optional<UserModel> findByUsername(String username) {
        return userRepository.findByUsuario(username);
    }

    public UserModel save(UserModel user) {
        return userRepository.save(user);
    }

    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
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

