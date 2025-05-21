package com.back.agricultorservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SeguridadConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/api/parcialidades/**").permitAll()
                                .requestMatchers("/api/transportistas/**").permitAll()
                                .requestMatchers("/api/medidas/**").permitAll()
                                .requestMatchers("/api/solicituddes/**").permitAll()
                                .requestMatchers("/api/transportes/**").permitAll()
                                //.requestMatchers(HttpMethod.PUT).permitAll()
                                //.anyRequest()
                                //.authenticated()
                                .anyRequest().permitAll()
                );
//                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
//                .sessionManagement((session) -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        return http.build();
    }
}
