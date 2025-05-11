package com.spring.security.jwt.repository;

import com.spring.security.jwt.model.EstadoCuentaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstadoCuentaRepository extends JpaRepository<EstadoCuentaModel, Integer> {
    List<EstadoCuentaModel> findByActivoTrue();
    Optional<EstadoCuentaModel> findByNombreEstado(String nombreEstado);
}
