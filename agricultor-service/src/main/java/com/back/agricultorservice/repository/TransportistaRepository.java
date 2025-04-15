package com.back.agricultorservice.repository;

import com.back.agricultorservice.model.TransportistaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransportistaRepository extends JpaRepository<TransportistaModel, String> {
    //Buscar transportistas activos de un agricultor
    List<TransportistaModel> findByAgricultor_NitAgricultorAndActivoTrue(String nitAgricultor);
    //Buscar trasnsportista y que este activo
    Optional<TransportistaModel> findByCuiTransportistaAndActivo(String cuiTransportista, boolean activo);
    // Verificar si un CUI existe
    boolean existsByCuiTransportista(String cuiTransportista);
    //Listar los transportistas activos
    List<TransportistaModel> findAllByActivoTrue();
}
