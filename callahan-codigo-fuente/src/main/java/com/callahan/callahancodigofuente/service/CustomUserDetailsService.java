package com.callahan.callahancodigofuente.service;

import com.callahan.callahancodigofuente.models.Usuario;
import com.callahan.callahancodigofuente.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String nombreUsuarioLogin) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuarioLogin)
                .orElseThrow(() -> new UsernameNotFoundException("Detective no encontrado: " + nombreUsuarioLogin));

        return User.builder()
                .username(usuario.getNombreUsuario())
                .password(usuario.getPassw())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(usuario.getRol())))
                .build();
    }
}
