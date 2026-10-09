package cl.duoc.ropaclinica.reportes.controller;

import cl.duoc.ropaclinica.reportes.dto.ReporteRequest;
import cl.duoc.ropaclinica.reportes.dto.ReporteResponse;
import cl.duoc.ropaclinica.reportes.model.TipoReporte;
import cl.duoc.ropaclinica.reportes.service.ReporteService;
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
@RequestMapping("/api/reportes")
@Tag(name = "Reporte")
@SecurityRequirement(name = "bearerAuth")
public class ReporteController {
    private final ReporteService service;

    public ReporteController(ReporteService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lista los registros (opcionalmente filtrados por tipo)")
    public List<ReporteResponse> listar(@RequestParam(required = false) TipoReporte tipo) {
        return service.listar(tipo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca un registro por id")
    public ReporteResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Crea un registro con sus detalles (el autor sale del token JWT)")
    public ResponseEntity<ReporteResponse> crear(@Valid @RequestBody ReporteRequest datos, Authentication authentication) {
        ReporteResponse creado = service.crear(datos, authentication.getName());
        return ResponseEntity.created(URI.create("/api/reportes/" + creado.id())).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un registro y reemplaza sus detalles")
    public ReporteResponse actualizar(@PathVariable Long id, @Valid @RequestBody ReporteRequest datos) {
        return service.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un registro (solo ADMIN)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
