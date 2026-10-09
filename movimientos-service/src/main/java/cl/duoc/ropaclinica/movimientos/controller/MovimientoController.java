package cl.duoc.ropaclinica.movimientos.controller;

import cl.duoc.ropaclinica.movimientos.dto.MovimientoRequest;
import cl.duoc.ropaclinica.movimientos.dto.MovimientoResponse;
import cl.duoc.ropaclinica.movimientos.model.TipoMovimiento;
import cl.duoc.ropaclinica.movimientos.service.MovimientoService;
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
@RequestMapping("/api/movimientos")
@Tag(name = "Movimiento")
@SecurityRequirement(name = "bearerAuth")
public class MovimientoController {
    private final MovimientoService service;

    public MovimientoController(MovimientoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lista los registros (opcionalmente filtrados por tipo)")
    public List<MovimientoResponse> listar(@RequestParam(required = false) TipoMovimiento tipo) {
        return service.listar(tipo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca un registro por id")
    public MovimientoResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Crea un registro con sus detalles (el autor sale del token JWT)")
    public ResponseEntity<MovimientoResponse> crear(@Valid @RequestBody MovimientoRequest datos, Authentication authentication) {
        MovimientoResponse creado = service.crear(datos, authentication.getName());
        return ResponseEntity.created(URI.create("/api/movimientos/" + creado.id())).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un registro y reemplaza sus detalles")
    public MovimientoResponse actualizar(@PathVariable Long id, @Valid @RequestBody MovimientoRequest datos) {
        return service.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un registro (solo ADMIN)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
