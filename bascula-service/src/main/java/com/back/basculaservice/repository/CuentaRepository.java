package com.back.basculaservice.repository;

import com.back.basculaservice.model.CuentaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentaRepository extends JpaRepository<CuentaModel, Integer> {
    List<CuentaModel> findByActivoTrue();
}
