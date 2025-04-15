package com.back.agricultorservice.service;

import com.back.agricultorservice.dto.TblAgricultorRequest;
import com.back.agricultorservice.dto.TblAgricultorResponse;
import com.back.agricultorservice.model.TblAgricultorModel;
import com.back.agricultorservice.repository.TblAgricultorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TblAgricultorService {
    private final TblAgricultorRepository agricultorRepository;

    public TblAgricultorResponse crearAgricultor(TblAgricultorRequest request, String usuario) {
        //Validar que no se dupliquen los NIT
        if (agricultorRepository.existsById(request.getNitAgricultor())) {
            throw new IllegalArgumentException("Ya existe un agricultor con ese NIT.");
        }

        TblAgricultorModel agricultor = new TblAgricultorModel();
        agricultor.setNitAgricultor(request.getNitAgricultor());
        agricultor.setNombre(request.getNombre());
        agricultor.setObservaciones(request.getObservaciones());
        agricultor.setActivo(true);
        agricultor.setFechaCreacion(LocalDateTime.now());
        agricultor.setUsuarioCreacion(usuario);

        agricultor = agricultorRepository.save(agricultor);
        return convertirAResponse(agricultor);
    }

    public TblAgricultorResponse actualizarAgricultor(String nit, TblAgricultorRequest request, String usuario) {
        TblAgricultorModel agricultor = agricultorRepository.findById(nit)
                .orElseThrow(() -> new IllegalArgumentException("Agricultor no encontrado"));

        agricultor.setNombre(request.getNombre());
        agricultor.setObservaciones(request.getObservaciones());
        agricultor.setFechaModificacion(LocalDateTime.now());
        agricultor.setUsuarioModificacion(usuario);

        agricultor = agricultorRepository.save(agricultor);
        return convertirAResponse(agricultor);
    }

    public void eliminarLogicamente(String nit, String usuario) {
        TblAgricultorModel agricultor = agricultorRepository.findById(nit)
                .orElseThrow(() -> new IllegalArgumentException("Agricultor no encontrado"));

        agricultor.setActivo(false);
        agricultor.setFechaModificacion(LocalDateTime.now());
        agricultor.setUsuarioModificacion(usuario);
        agricultorRepository.save(agricultor);
    }

    public TblAgricultorResponse buscarPorNit(String nit) {
        TblAgricultorModel agricultor = agricultorRepository.findById(nit)
                .orElseThrow(() -> new IllegalArgumentException("Agricultor no encontrado"));

        return convertirAResponse(agricultor);
    }

    public List<TblAgricultorResponse> listarActivos() {
        return agricultorRepository.findByActivoTrue()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    private TblAgricultorResponse convertirAResponse(TblAgricultorModel model) {
        TblAgricultorResponse response = new TblAgricultorResponse();
        response.setNitAgricultor(model.getNitAgricultor());
        response.setNombre(model.getNombre());
        response.setActivo(model.isActivo());
        response.setObservaciones(model.getObservaciones());
        response.setFechaCreacion(model.getFechaCreacion().toLocalDate());
        response.setUsuarioCreacion(model.getUsuarioCreacion());
        response.setFechaModificacion(model.getFechaModificacion() != null ? model.getFechaModificacion().toLocalDate() : null);
        response.setUsuarioModificacion(model.getUsuarioModificacion());
        return response;
    }
}
