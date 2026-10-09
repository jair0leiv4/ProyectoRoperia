package cl.duoc.ropaclinica.bajas.controller;

import cl.duoc.ropaclinica.bajas.dto.RegistroBajaRequest;
import cl.duoc.ropaclinica.bajas.dto.RegistroBajaResponse;
import cl.duoc.ropaclinica.bajas.model.MotivoBaja;
import cl.duoc.ropaclinica.bajas.service.RegistroBajaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bajas")
@Tag(name = "RegistroBaja")
@SecurityRequirement(name = "bearerAuth")
public class RegistroBajaController {
    private final RegistroBajaService service;

    public RegistroBajaController(RegistroBajaService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lista los registros (opcionalmente filtrados por motivo)")
    public List<RegistroBajaResponse> listar(@RequestParam(required = false) MotivoBaja motivo) {
        return service.listar(motivo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca un registro por id")
    public RegistroBajaResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Crea un registro con sus detalles (el autor sale del token JWT)")
    public ResponseEntity<RegistroBajaResponse> crear(@Valid @RequestBody RegistroBajaRequest datos, Authentication authentication) {
        RegistroBajaResponse creado = service.crear(datos, authentication.getName());
        return ResponseEntity.created(URI.create("/api/bajas/" + creado.id())).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un registro y reemplaza sus detalles")
    public RegistroBajaResponse actualizar(@PathVariable Long id, @Valid @RequestBody RegistroBajaRequest datos) {
        return service.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un registro (solo ADMIN)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
