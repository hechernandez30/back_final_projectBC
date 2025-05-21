package com.spring.security.jwt.config;


import com.spring.security.jwt.filter.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception{
        http
                .cors(withDefaults())
                .csrf(crf -> crf.disable())
                .authorizeHttpRequests((authorize) -> authorize
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                .requestMatchers("/api/estados/**").permitAll()
                                .requestMatchers("/api/cuentas/**").permitAll()
                                .requestMatchers("/api/parcialidades/**").permitAll()
                                .requestMatchers("/api/transportes/**").permitAll()
                                .requestMatchers("/api/transportistas/**").permitAll()
                                .requestMatchers("/api/usuarios/**").permitAll()
//                                .requestMatchers(HttpMethod.GET,"/api/estados/**").permitAll()
//                                .requestMatchers(HttpMethod.PUT,"/api/estados/**").permitAll()
                                .anyRequest().authenticated()
                        )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder () {
        return  new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return  authenticationConfiguration.getAuthenticationManager();
    }
}

