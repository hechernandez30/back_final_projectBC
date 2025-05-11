package com.back.basculaservice.repository;

import com.back.basculaservice.model.ParcialidadModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParcialidadRepository extends JpaRepository<ParcialidadModel, Integer> {
    List<ParcialidadModel> findByActivoTrue();
    List<ParcialidadModel> findBySolicitudIdAndActivoTrue(int solicitudId);
}
