package com.back.agricultorservice.repository;

import com.back.agricultorservice.model.ParcialidadModel;
import com.back.agricultorservice.model.SolicitudModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParcialidadRepository extends JpaRepository<ParcialidadModel, Integer> {
    List<ParcialidadModel> findBySolicitudAndActivoTrue(SolicitudModel solicitud);
    long countBySolicitudAndActivoTrue(SolicitudModel solicitud);
}
