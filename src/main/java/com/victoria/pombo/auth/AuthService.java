package com.victoria.pombo.auth;

import com.victoria.pombo.model.entity.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtService jwtService;

    public AuthService(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    public String authenticate(Authentication authentication) {
        return jwtService.getGenerateToken(authentication);
    }

    public Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioAutenticado = null;

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof Usuario) {
                UserDetails userDetails = (Usuario) principal;
                usuarioAutenticado = (Usuario) userDetails;
            }
        }
        return usuarioAutenticado;
    }
}