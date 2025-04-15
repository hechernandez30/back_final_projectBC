package com.back.agricultorservice.repository;

import com.back.agricultorservice.model.MedidaPesoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedidaPesoRepository extends JpaRepository<MedidaPesoModel, Integer> {
    // Buscar medidas de peso activas
    List<MedidaPesoModel> findByActivoTrue();

    // Buscar por abreviatura (ej: "Kg")
    //MedidaPesoModel findByAbreviaturaPeso(String abreviatura);
    Optional<MedidaPesoModel> findByAbreviaturaPeso(String abreviaturaPeso);

}
