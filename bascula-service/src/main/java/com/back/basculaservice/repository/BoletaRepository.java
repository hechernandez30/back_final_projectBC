package com.back.basculaservice.repository;

import com.back.basculaservice.model.BoletaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoletaRepository extends JpaRepository<BoletaModel, Integer> {
    List<BoletaModel> findByActivoTrue();
}
