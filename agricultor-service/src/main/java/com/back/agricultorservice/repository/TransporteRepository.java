package com.back.agricultorservice.repository;

import com.back.agricultorservice.model.TransporteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransporteRepository extends JpaRepository<TransporteModel, String> {
    //Buscar transportes por agricultor (NIT) y activos
    List<TransporteModel> findByAgricultor_NitAgricultorAndActivoTrue(String nitAgricultor);
    //Verificar si una placa existe
    boolean existsByPlacaTransporte(String placaTransporte);
    //Listar todos los transportes activos
    List<TransporteModel> findByActivoTrue();
}
