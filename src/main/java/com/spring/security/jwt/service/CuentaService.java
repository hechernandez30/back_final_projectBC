package com.spring.security.jwt.service;

import com.spring.security.jwt.dto.CuentaRequestDto;
import com.spring.security.jwt.dto.CuentaResponseDto;
import com.spring.security.jwt.model.CuentaModel;
import com.spring.security.jwt.model.EstadoCuentaModel;
import com.spring.security.jwt.model.HistorialEstadoCuentaModel;
import com.spring.security.jwt.model.UserModel;
import com.spring.security.jwt.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CuentaService {
    private final CuentaRepository cuentaRepository;
    private final TransportistaAutorizadoRepository transportistaAutorizadoRepository;
    private final EstadoCuentaRepository estadoCuentaRepository;
    private final HistorialEstadoCuentaRepository historialEstadoCuentaRepository;
    private final UserRepository userRepository;
    // Metodo para crear una nueva cuenta
    public CuentaResponseDto crearCuenta(CuentaRequestDto request, String usuarioCreacion) {
        // Validación: nombre de usuario debe venir en el header para nuestro ejemplo
        if (usuarioCreacion == null || usuarioCreacion.isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario es requerido en el header 'X-Usuario'");
        }

        UserModel agricultor = userRepository.findByNit(request.getNitAgricultor())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró un agricultor con el NIT proporcionado: " + request.getNitAgricultor()));

        if (agricultor == null) {
            throw new IllegalArgumentException("No se encontró un agricultor con el NIT proporcionado: " + request.getNitAgricultor());
        }

        EstadoCuentaModel estado = estadoCuentaRepository.findById(request.getEstadoCuentaId())
                .orElseThrow(() -> new IllegalArgumentException("EstadoCuenta no encontrado"));

        if (usuarioCreacion == null || usuarioCreacion.isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario es requerido en el header 'X-Usuario'");
        }

        int nuevoNumero = cuentaRepository.findMaxNumeroCuenta().orElse(0) + 1;
        // Crear nueva cuenta
        CuentaModel cuenta = new CuentaModel();
        cuenta.setNumeroCuenta(nuevoNumero);
        cuenta.setAgricultor(agricultor);
        cuenta.setEstadoCuenta(estado);
        cuenta.setPesoAcordado(request.getPesoAcordado());
        cuenta.setCantParcialidades(request.getCantParcialidades());
        cuenta.setPesoCadaParcialidad(request.getPesoCadaParcialidad());
        cuenta.setMedidaPeso(request.getMedidaPeso());
        cuenta.setObservaciones(request.getObservaciones());
        cuenta.setFechaCreacion(LocalDate.now());
        cuenta.setUsuarioCreacion(usuarioCreacion);
        // Asignar estado por defecto para cuando la cuenta se cree
        EstadoCuentaModel estadoPorDefecto = new EstadoCuentaModel();
        estadoPorDefecto.setEstadoCuentaId(1);
        cuenta.setEstadoCuenta(estadoPorDefecto);
        CuentaModel guardada = cuentaRepository.save(cuenta);

        // Registrar en historial de estado
        HistorialEstadoCuentaModel historial = new HistorialEstadoCuentaModel();
        historial.setCuentaId(guardada);
        historial.setEstadoCuentaAnterior(null); // Al ser la primera vez, no hay estado anterior
        historial.setEstadoCuentaNuevo(estadoPorDefecto);
        historial.setUsuarioId(usuarioCreacion); // Ahora es un String
        historial.setObservaciones(request.getObservaciones());
        historial.setFechaCambio(LocalDate.now());
        historialEstadoCuentaRepository.save(historial);

        return mapToDto(guardada);
    }
    // Metodo para listar todas las cuentas de la tabla Cuentas
    public List<CuentaResponseDto> listarCuentas() {
        return cuentaRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }
    // Metodo para modificar una cuenta existente
    public CuentaResponseDto actualizarCuenta(int id, CuentaRequestDto request, String usuarioModificacion) {
        // Validación: nombre de usuario debe venir en el header
        if (usuarioModificacion == null || usuarioModificacion.isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario es requerido en el header 'X-Usuario'");
        }

        CuentaModel cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cuenta no encontrada"));

        UserModel agricultor = userRepository.findByNit(request.getNitAgricultor())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró un agricultor con el NIT proporcionado: " + request.getNitAgricultor()));

        if (agricultor == null) {
            throw new IllegalArgumentException("No se encontró un agricultor con el NIT proporcionado: " + request.getNitAgricultor());
        }

        EstadoCuentaModel estadoNuevo = estadoCuentaRepository.findById(request.getEstadoCuentaId())
                .orElseThrow(() -> new IllegalArgumentException("EstadoCuenta no encontrado"));

        EstadoCuentaModel estadoAnterior = cuenta.getEstadoCuenta();

        // Actualizar campos
        cuenta.setNumeroCuenta(request.getNumeroCuenta());
        cuenta.setAgricultor(agricultor);
        cuenta.setEstadoCuenta(estadoNuevo);
        cuenta.setPesoAcordado(request.getPesoAcordado());
        cuenta.setCantParcialidades(request.getCantParcialidades());
        cuenta.setPesoCadaParcialidad(request.getPesoCadaParcialidad());
        cuenta.setMedidaPeso(request.getMedidaPeso());
        cuenta.setObservaciones(request.getObservaciones());
        cuenta.setFechaModificacion(LocalDate.now());
        cuenta.setUsuarioModificacion(usuarioModificacion);
        CuentaModel actualizada = cuentaRepository.save(cuenta);

        // Si cambió el estado, registrar en historial
        if (estadoAnterior != null &&
                !estadoAnterior.getEstadoCuentaId().equals(estadoNuevo.getEstadoCuentaId())) {

            HistorialEstadoCuentaModel historial = new HistorialEstadoCuentaModel();
            historial.setCuentaId(actualizada);
            historial.setEstadoCuentaAnterior(estadoAnterior);
            historial.setEstadoCuentaNuevo(estadoNuevo);
            historial.setUsuarioId(usuarioModificacion);
            historial.setFechaCambio(LocalDate.now());
            historial.setObservaciones("Cambio automático desde servicio");
            historialEstadoCuentaRepository.save(historial);
        }
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:3001/api/solicitudes/actualizar-estado/" + cuenta.getNumeroCuenta();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(estadoNuevo.getNombreEstado(), headers);
            restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
        } catch (Exception ex) {
            System.err.println("Error al sincronizar el estado con el Agricultor: " + ex.getMessage());
        }
        return mapToDto(actualizada);
    }

    // Metodo para convertir del request al response
    private CuentaResponseDto mapToDto(CuentaModel model) {
        CuentaResponseDto dto = new CuentaResponseDto();
        dto.setCuentaId(model.getCuentaId());
        dto.setNumeroCuenta(model.getNumeroCuenta());
        dto.setNitAgricultor(model.getAgricultor());
        dto.setNitAgricultorNit(model.getAgricultor().getNit());
        dto.setEstadoCuentaId(model.getEstadoCuenta().getEstadoCuentaId());
        dto.setEstadoCuenta(model.getEstadoCuenta().getNombreEstado());
        dto.setPesoAcordado(model.getPesoAcordado());
        dto.setCantParcialidades(model.getCantParcialidades());
        dto.setPesoCadaParcialidad(model.getPesoCadaParcialidad());
        dto.setMedidaPeso(model.getMedidaPeso());
        dto.setObservaciones(model.getObservaciones());
        dto.setFechaCreacion(model.getFechaCreacion());
        dto.setUsuarioCreacion(model.getUsuarioCreacion());
        dto.setFechaModificacion(model.getFechaModificacion());
        dto.setUsuarioModificacion(model.getUsuarioModificacion());
        return dto;
    }
    public CuentaResponseDto obtenerPorId(int id) {
        CuentaModel cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + id));
        return mapToDto(cuenta); // Usa tu mismo método que usas en listar
    }

}
