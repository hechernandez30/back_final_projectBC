package com.spring.security.jwt.repository;

import com.spring.security.jwt.model.HistorialEstadoCuentaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialEstadoCuentaRepository extends JpaRepository<HistorialEstadoCuentaModel, Integer> {
}
