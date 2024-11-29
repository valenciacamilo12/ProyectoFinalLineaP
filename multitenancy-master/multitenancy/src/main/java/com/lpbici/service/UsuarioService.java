package com.lpbici.service;

import com.lpbici.entity.Usuario;
import com.lpbici.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public List<Usuario> lista(){
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getOne(int id){
        return Optional.ofNullable(usuarioRepository.findOne(id));
    }

    public Optional<Usuario> getById(int id){
        return Optional.ofNullable(usuarioRepository.findOne(id));
    }

    public Optional<Usuario> getByNombreUsuario(String nombreUsuario){
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    public void save(Usuario usuario){
        usuarioRepository.save(usuario);
    }

    public boolean existsById(int id){
        return usuarioRepository.exists(id);
    }

    public boolean existsByNombreusuario(String nombreUsuario){
        return usuarioRepository.existsByNombreUsuario(nombreUsuario);
    }
}
