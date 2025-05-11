package com.spring.security.jwt.repository;

import com.spring.security.jwt.model.TransporteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransporteRepository extends JpaRepository<TransporteModel, String> {
    List<TransporteModel> findByActivoTrue();
}
