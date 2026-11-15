package com.dv.oficina.config;

import com.dv.oficina.model.Perfil;
import com.dv.oficina.model.Usuario;
import com.dv.oficina.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String username;

    @Value("${app.admin.password}")
    private String password;

    @Override
    public void run(ApplicationArguments args) {
        if (usuarioRepository.count() > 0) {
            return;
        }
        Usuario admin = new Usuario();
        admin.setUsername(username);
        admin.setSenhaHash(passwordEncoder.encode(password));
        admin.setPerfil(Perfil.ADMIN);
        usuarioRepository.save(admin);
    }
}