package cl.duoc.ropaclinica.inventario.controller;

import cl.duoc.ropaclinica.inventario.dto.PrendaRequest;
import cl.duoc.ropaclinica.inventario.dto.PrendaResponse;
import cl.duoc.ropaclinica.inventario.service.PrendaStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prendas")
@Tag(name = "Prendas en stock")
@SecurityRequirement(name = "bearerAuth")
public class PrendaStockController {
    private final PrendaStockService service;

    public PrendaStockController(PrendaStockService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lista el stock (opcionalmente filtrado por tipoPrendaId)")
    public List<PrendaResponse> listar(@RequestParam(required = false) Long tipoPrendaId) {
        return service.listar(tipoPrendaId);
    }

    @GetMapping("/bajo-stock")
    @Operation(summary = "Lista las prendas con cantidad menor o igual al stock mínimo")
    public List<PrendaResponse> bajoStock() {
        return service.bajoStock();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca una prenda por id")
    public PrendaResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Registra una prenda en stock")
    public ResponseEntity<PrendaResponse> crear(@Valid @RequestBody PrendaRequest datos) {
        PrendaResponse creada = service.crear(datos);
        return ResponseEntity.created(URI.create("/api/prendas/" + creada.id())).body(creada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza una prenda en stock")
    public PrendaResponse actualizar(@PathVariable Long id, @Valid @RequestBody PrendaRequest datos) {
        return service.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una prenda (solo ADMIN)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
