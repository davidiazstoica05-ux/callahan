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
                        .requestMatchers("/logistica/**").hasAnyRole("LOGISTICA", "ADMIN")

                        .requestMatchers("/rrhh/**").hasAnyRole("RRHH", "ADMIN")

                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .requestMatchers("/login", "/css/**", "/js/**", "/img/**", "/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .requestCache(cache -> {
                    HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
                    requestCache.setMatchingRequestParameterName(null);
                    cache.requestCache(requestCache);
                })
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/acceso-denegado")
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                );

        http.csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
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
