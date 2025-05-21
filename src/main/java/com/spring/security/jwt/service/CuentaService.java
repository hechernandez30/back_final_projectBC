package com.spring.security.jwt.service;

import com.spring.security.jwt.dto.CuentaRequestDto;
import com.spring.security.jwt.dto.CuentaResponseDto;
import com.spring.security.jwt.model.CuentaModel;
import com.spring.security.jwt.model.EstadoCuentaModel;
import com.spring.security.jwt.model.HistorialEstadoCuentaModel;
import com.spring.security.jwt.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        // Validación: verificar si existe el agricultor
        boolean agricultorExiste = userRepository.existsByNit(String.valueOf(request.getNitAgricultor()));
        if (!agricultorExiste) {
            throw new IllegalArgumentException("No se encontró un agricultor con el NIT proporcionado: " + request.getNitAgricultor());
        }

        // Crear nueva cuenta
        CuentaModel cuenta = new CuentaModel();
        cuenta.setNumeroCuenta(request.getNumeroCuenta());
        cuenta.setNit(request.getNitAgricultor());
        cuenta.setEstadoCuenta(request.getEstadoCuenta());
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
        //Validación: nombre de usuario debe venir en el header
        if (usuarioModificacion == null || usuarioModificacion.isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario es requerido en el header 'X-Usuario'");
        }

        CuentaModel cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cuenta no encontrada"));

        EstadoCuentaModel estadoAnterior = cuenta.getEstadoCuenta();
        EstadoCuentaModel estadoNuevo = request.getEstadoCuenta();

        // Actualizar campos
        cuenta.setNumeroCuenta(request.getNumeroCuenta());
        cuenta.setNit(request.getNitAgricultor());
        cuenta.setEstadoCuenta(request.getEstadoCuenta());
        cuenta.setPesoAcordado(request.getPesoAcordado());
        cuenta.setCantParcialidades(request.getCantParcialidades());
        cuenta.setPesoCadaParcialidad(request.getPesoCadaParcialidad());
        cuenta.setMedidaPeso(request.getMedidaPeso());
        cuenta.setObservaciones(request.getObservaciones());
        cuenta.setFechaModificacion(LocalDate.now());
        cuenta.setUsuarioModificacion(usuarioModificacion);
        CuentaModel actualizada = cuentaRepository.save(cuenta);

        // Si cambió el estado, registrar en historial
        if (estadoAnterior != null && estadoNuevo != null && estadoAnterior.getEstadoCuentaId() != estadoNuevo.getEstadoCuentaId()) {
            HistorialEstadoCuentaModel historial = new HistorialEstadoCuentaModel();
            historial.setCuentaId(cuenta);
            historial.setEstadoCuentaAnterior(estadoAnterior);
            historial.setEstadoCuentaNuevo(estadoNuevo);
            historial.setUsuarioId(usuarioModificacion);
            historial.setFechaCambio(LocalDate.now());
            historial.setObservaciones("Cambio automático desde servicio");
            historialEstadoCuentaRepository.save(historial);
        }
        return mapToDto(actualizada);
    }
    // Metodo para convertir del request al response
    private CuentaResponseDto mapToDto(CuentaModel model) {
        CuentaResponseDto dto = new CuentaResponseDto();
        dto.setCuentaId(model.getCuentaId());
        dto.setNitAgricultor(model.getNit());
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
}
