package com.callahan.callahancodigofuente.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authz -> authz

                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/css/**",
                                "/js/**",
                                "/api/detectives/registro",
                                "/api/detectives/iniciarSesion",
                                "/h2-console/**",
                                "/acceso-denegado",
                                "/403.html",
                                "/error"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .requestCache(cache -> {
                    HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
                    requestCache.setMatchingRequestParameterName(null);
                    cache.requestCache(requestCache);
                })
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/acceso-denegado") // Captura a los que no tienen el rol necesario
                        .authenticationEntryPoint((request, response, authException) -> {
                            // Captura a los intrusos que ni siquiera han iniciado sesión
                            response.sendRedirect("/acceso-denegado");
                        })
                );

        http.csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
                .disable()
        );

        http.headers(headers -> headers
                .frameOptions(opts -> opts.disable())
        );

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder(10);
    }


}
