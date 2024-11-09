package com.victoria.pombo.auth;

import com.victoria.pombo.model.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {
    public final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository userLogin) {
        this.usuarioRepository = userLogin;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Usuário " + username + " não encontrado")
                );
    }
}
