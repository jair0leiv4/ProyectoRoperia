package cl.duoc.ropaclinica.inventario.dto;

import jakarta.validation.constraints.*;

public record PrendaRequest(
        @NotNull @Positive Long tipoPrendaId,
        @NotBlank @Size(max = 20) String talla,
        @NotNull @Min(0) Integer cantidad,
        @NotNull @Min(0) Integer stockMinimo,
        @NotBlank @Size(max = 100) String ubicacion) {}
