package com.callahan.callahancodigofuente.repository;

import com.callahan.callahancodigofuente.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository <Usuario, Long> {


        Usuario findUsuarioByIdUsuario(Long id);


}


