package com.callahan.callahancodigofuente.repository;

import com.callahan.callahancodigofuente.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository <Usuario, Long> {
}


