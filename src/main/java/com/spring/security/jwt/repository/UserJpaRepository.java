package com.spring.security.jwt.repository;

import com.spring.security.jwt.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserJpaRepository  extends JpaRepository<UserModel, Integer> {
    Optional<UserModel> findByNit(String nit);
    List<UserModel> findByActivoTrue();
}
