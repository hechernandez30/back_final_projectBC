package com.back.agricultorservice.repository;

import com.back.agricultorservice.model.TblAgricultorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TblAgricultorRepository extends JpaRepository<TblAgricultorModel, String> {
    //Buscar agricultores activos
    List<TblAgricultorModel> findByActivoTrue();
    
    boolean existsByNitAgricultor(String nitAgricultor);
}
