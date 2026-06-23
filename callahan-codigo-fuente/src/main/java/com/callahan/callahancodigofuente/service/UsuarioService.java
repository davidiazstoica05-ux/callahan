package com.callahan.callahancodigofuente.service;

import com.callahan.callahancodigofuente.models.Usuario;
import com.callahan.callahancodigofuente.repository.UsuarioRepository;
import com.callahan.callahancodigofuente.service.base.BaseService;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService extends BaseService<Usuario, Long, UsuarioRepository> {
}
