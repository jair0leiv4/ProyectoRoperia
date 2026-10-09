package cl.duoc.ropaclinica.auth.config;

import cl.duoc.ropaclinica.auth.model.Rol;
import cl.duoc.ropaclinica.auth.model.Usuario;
import cl.duoc.ropaclinica.auth.repository.RolRepository;
import cl.duoc.ropaclinica.auth.repository.UsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Al arrancar crea los roles y el primer ADMIN (si no existen) para poder crear al resto de usuarios. */
@Configuration
public class DatosIniciales {
    @Bean
    CommandLineRunner cargarDatos(RolRepository roles, UsuarioRepository usuarios, PasswordEncoder encoder,
            @Value("${app.admin.username}") String username,
            @Value("${app.admin.password}") String password) {
        return args -> {
            for (String nombre : List.of("ADMIN", "BODEGA", "DIGITADOR")) {
                if (roles.findByNombre(nombre).isEmpty()) {
                    roles.save(new Rol(nombre));
                }
            }
            if (!usuarios.existsByUsername(username)) {
                Rol admin = roles.findByNombre("ADMIN").orElseThrow();
                usuarios.save(new Usuario(username, encoder.encode(password), admin));
            }
        };
    }
}
