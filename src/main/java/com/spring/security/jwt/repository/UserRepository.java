package com.spring.security.jwt.repository;

import com.spring.security.jwt.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository implements IUserRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public UserModel findByName(String user) {
        String SQL = "SELECT * FROM usuario WHERE usuario = ?"; //En lugar de usuario estaba nombre
        return jdbcTemplate.queryForObject(SQL, new Object[]{user},
                new BeanPropertyRowMapper<>(UserModel.class));
    }

    @Override
    public boolean existsByNit(String nit){
        String sql = "SELECT * FROM usuario WHERE nit = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, nit);
        return count != null && count > 0;
    };
    @Override
    public UserModel findByNombreUsuario(String nombreUsuario) {
        String sql = "SELECT * FROM usuario WHERE usuario = ?"; //En lugar de usuario estaba nombre
        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{nombreUsuario},
                new BeanPropertyRowMapper<>(UserModel.class)
        );
    }
}
