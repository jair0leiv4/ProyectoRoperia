package cl.duoc.ropaclinica.auth.service;

import cl.duoc.ropaclinica.auth.dto.LoginRequest;
import cl.duoc.ropaclinica.auth.dto.UsuarioRequest;
import cl.duoc.ropaclinica.auth.dto.UsuarioResponse;
import cl.duoc.ropaclinica.auth.model.Rol;
import cl.duoc.ropaclinica.auth.model.Usuario;
import cl.duoc.ropaclinica.auth.repository.RolRepository;
import cl.duoc.ropaclinica.auth.repository.UsuarioRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class UsuarioService {
    private final UsuarioRepository repository;
    private final RolRepository rolRepository;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repository, RolRepository rolRepository, PasswordEncoder encoder) {
        this.repository = repository;
        this.rolRepository = rolRepository;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return repository.findAll().stream().map(UsuarioResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscar(Long id) {
        return UsuarioResponse.de(obtener(id));
    }

    public UsuarioResponse crear(UsuarioRequest datos) {
        if (repository.existsByUsername(datos.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya existe");
        }
        Usuario usuario = new Usuario(datos.username(), encoder.encode(datos.password()), buscarRol(datos.rol()));
        return UsuarioResponse.de(repository.save(usuario));
    }

    public UsuarioResponse actualizar(Long id, UsuarioRequest datos) {
        Usuario usuario = obtener(id);
        if (!usuario.getUsername().equals(datos.username()) && repository.existsByUsername(datos.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya existe");
        }
        usuario.setUsername(datos.username());
        usuario.setPassword(encoder.encode(datos.password()));
        usuario.setRol(buscarRol(datos.rol()));
        return UsuarioResponse.de(repository.save(usuario));
    }

    public void eliminar(Long id) {
        repository.delete(obtener(id));
    }

    @Transactional(readOnly = true)
    public UsuarioResponse autenticar(LoginRequest datos) {
        Usuario usuario = repository.findByUsername(datos.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas"));
        if (!encoder.matches(datos.password(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas");
        }
        return UsuarioResponse.de(usuario);
    }

    private Usuario obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro no encontrado"));
    }

    private Rol buscarRol(String nombre) {
        return rolRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol inexistente: " + nombre));
    }
}
