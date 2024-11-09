package com.victoria.pombo.auth;

import com.victoria.pombo.exception.OpomboException;
import com.victoria.pombo.model.entity.Usuario;
import com.victoria.pombo.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public AuthService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String authenticate(Authentication authentication) {
        return jwtService.getGenerateToken(authentication);
    }

    public Usuario getUsuarioAutenticado() throws OpomboException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioAutenticado = null;

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            Jwt jwt = (Jwt) principal;
            String login = jwt.getClaim("sub");

            usuarioAutenticado = usuarioRepository.findByEmail(login)
                    .orElseThrow(() -> new OpomboException("Usuário não encontrado"));
        }

        if (usuarioAutenticado == null) {
            throw new OpomboException("Usuário não encontrado");
        }

        return usuarioAutenticado;
    }
}