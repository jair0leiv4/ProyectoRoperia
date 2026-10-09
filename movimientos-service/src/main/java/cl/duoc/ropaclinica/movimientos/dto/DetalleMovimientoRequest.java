package cl.duoc.ropaclinica.movimientos.dto;

import jakarta.validation.constraints.*;

public record DetalleMovimientoRequest(@NotNull @Positive Long prendaId, @NotNull @Positive Integer cantidad) {}
