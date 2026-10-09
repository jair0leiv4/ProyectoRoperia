package cl.duoc.ropaclinica.auth.controller;

import cl.duoc.ropaclinica.auth.dto.LoginRequest;
import cl.duoc.ropaclinica.auth.dto.UsuarioResponse;
import cl.duoc.ropaclinica.auth.security.JwtService;
import cl.duoc.ropaclinica.auth.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación")
public class AuthController {
    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public AuthController(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    @Operation(summary = "Inicia sesión y entrega un token JWT (Bearer)")
    public Map<String, String> login(@Valid @RequestBody LoginRequest request) {
        UsuarioResponse usuario = usuarioService.autenticar(request);
        return Map.of("token", jwtService.generarToken(usuario.username(), usuario.rol()), "tipo", "Bearer");
    }
}
