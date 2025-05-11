package com.spring.security.jwt.repository;

import com.spring.security.jwt.model.TransportistaAutorizadoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransportistaAutorizadoRepository extends JpaRepository<TransportistaAutorizadoModel, Integer> {
    Optional<TransportistaAutorizadoModel> findByCuiTransportista(String cuiTransportista);
    List<TransportistaAutorizadoModel> findByActivoTrue();
    boolean existsByCuiTransportista(String cui);
}
