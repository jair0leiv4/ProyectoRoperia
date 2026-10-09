package cl.duoc.ropaclinica.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Estado")
public class EstadoController {
    @GetMapping("/api/publico/estado")
    @Operation(summary = "Comprueba que el microservicio está activo (público)")
    public Map<String, String> estado() {
        return Map.of("estado", "OK", "servicio", "auth-service");
    }
}
