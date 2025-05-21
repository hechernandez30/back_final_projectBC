package com.back.agricultorservice.repository;

import com.back.agricultorservice.model.SolicitudModel;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<SolicitudModel, Long> {
    //Buscar solicitudes por agricultor (NIT)
    List<SolicitudModel> findByNitAgricultor(String nitAgricultor);

    //Buscar solicitudes pendientes (ej: estado = "Pendiente")
    //List<SolicitudModel> findByEstado(String estado);

    //Contar solicitudes activos de un agricultor
    //long countByAgricultor_NitAgricultorAndEstado(String nitAgricultor, String estado);
    //Listar todas las solicitudes
    List<SolicitudModel> findAll();
}
