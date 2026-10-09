package cl.duoc.ropaclinica.inventario.controller;

import cl.duoc.ropaclinica.inventario.dto.TipoPrendaRequest;
import cl.duoc.ropaclinica.inventario.dto.TipoPrendaResponse;
import cl.duoc.ropaclinica.inventario.service.TipoPrendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tipos-prenda")
@Tag(name = "Tipos de prenda")
@SecurityRequirement(name = "bearerAuth")
public class TipoPrendaController {
    private final TipoPrendaService service;

    public TipoPrendaController(TipoPrendaService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lista los tipos de prenda")
    public List<TipoPrendaResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca un tipo de prenda por id")
    public TipoPrendaResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Crea un tipo de prenda")
    public ResponseEntity<TipoPrendaResponse> crear(@Valid @RequestBody TipoPrendaRequest datos) {
        TipoPrendaResponse creado = service.crear(datos);
        return ResponseEntity.created(URI.create("/api/tipos-prenda/" + creado.id())).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un tipo de prenda")
    public TipoPrendaResponse actualizar(@PathVariable Long id, @Valid @RequestBody TipoPrendaRequest datos) {
        return service.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un tipo de prenda (solo ADMIN; falla si tiene prendas)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
