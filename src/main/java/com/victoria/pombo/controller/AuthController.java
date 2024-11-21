package com.victoria.pombo.controller;

import com.victoria.pombo.auth.AuthService;
import com.victoria.pombo.exception.OpomboException;
import com.victoria.pombo.model.dto.UsuarioDTO;
import com.victoria.pombo.model.entity.Usuario;
import com.victoria.pombo.model.enums.Role;
import com.victoria.pombo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {

    @Autowired
    private AuthService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Método de login padronizado -> Basic Auth
     * <p>
     * O parâmetro Authentication já encapsula login (username) e senha (password)
     * Basic <Base64 encoded username and password>
     *
     * @param authentication
     * @return o JWT gerado
     */
    @PostMapping("/authenticate")
    public String authenticate(Authentication authentication) {
        return authenticationService.authenticate(authentication);
    }

    @PostMapping("/novo-usuario")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void registrarUsuario(@RequestBody UsuarioDTO novoUsuarioDTO) throws OpomboException {

    	Usuario novoUsuario = Usuario.fromDTO(novoUsuarioDTO);
    	
        String senhaCifrada = passwordEncoder.encode(novoUsuario.getSenha());

        novoUsuario.setSenha(senhaCifrada);
        novoUsuario.setRole(Role.USER);

        usuarioService.inserir(novoUsuario);
    }

}