package com.back.basculaservice.repository;

import com.back.basculaservice.model.MedidaPesoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedidaPesoRepository extends JpaRepository<MedidaPesoModel, Integer> {
    List<MedidaPesoModel> findByActivoTrue();
    Optional<MedidaPesoModel> findByAbreviaturaPeso(String abreviaturaPeso);
}
