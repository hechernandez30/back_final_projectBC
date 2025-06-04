package com.spring.security.jwt.repository;

import com.spring.security.jwt.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByNit(String nit);
    Optional<UserModel> findByUsuario(String username);
    boolean existsByNit(String nit);

    List<UserModel> findByActivoTrue();
}
