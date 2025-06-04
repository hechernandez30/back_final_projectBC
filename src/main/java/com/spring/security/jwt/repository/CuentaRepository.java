package com.spring.security.jwt.repository;

import com.spring.security.jwt.dto.CuentaResponseDto;
import com.spring.security.jwt.model.CuentaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<CuentaModel, Integer> {
    List<CuentaModel> findByCuentaId(Integer cuentaId);
    Optional<CuentaModel> findByAgricultor_Nit(String nit);
    @Query("SELECT MAX(c.numeroCuenta) FROM CuentaModel c")
    Optional<Integer> findMaxNumeroCuenta();

}
