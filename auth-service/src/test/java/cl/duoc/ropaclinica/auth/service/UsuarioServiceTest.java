package cl.duoc.ropaclinica.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import cl.duoc.ropaclinica.auth.dto.LoginRequest;
import cl.duoc.ropaclinica.auth.dto.UsuarioRequest;
import cl.duoc.ropaclinica.auth.dto.UsuarioResponse;
import cl.duoc.ropaclinica.auth.model.Rol;
import cl.duoc.ropaclinica.auth.model.Usuario;
import cl.duoc.ropaclinica.auth.repository.RolRepository;
import cl.duoc.ropaclinica.auth.repository.UsuarioRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @Mock UsuarioRepository repository;
    @Mock RolRepository rolRepository;
    @Mock PasswordEncoder encoder;
    @InjectMocks UsuarioService service;

    @Test
    void crearGuardaUsuarioConPasswordHasheado() {
        when(repository.existsByUsername("ana")).thenReturn(false);
        when(rolRepository.findByNombre("BODEGA")).thenReturn(Optional.of(new Rol("BODEGA")));
        when(encoder.encode("secreto1")).thenReturn("HASH");
        when(repository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        UsuarioResponse resp = service.crear(new UsuarioRequest("ana", "secreto1", "BODEGA"));

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).save(captor.capture());
        assertEquals("HASH", captor.getValue().getPassword());
        assertEquals("BODEGA", resp.rol());
    }

    @Test
    void crearUsuarioDuplicadoLanza409() {
        when(repository.existsByUsername("ana")).thenReturn(true);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.crear(new UsuarioRequest("ana", "secreto1", "BODEGA")));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void loginConPasswordIncorrectoLanza401() {
        Usuario usuario = new Usuario("ana", "HASH", new Rol("BODEGA"));
        when(repository.findByUsername("ana")).thenReturn(Optional.of(usuario));
        when(encoder.matches("mala", "HASH")).thenReturn(false);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.autenticar(new LoginRequest("ana", "mala")));
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    }
}
