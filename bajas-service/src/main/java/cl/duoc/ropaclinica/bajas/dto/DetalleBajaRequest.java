package cl.duoc.ropaclinica.bajas.dto;

import jakarta.validation.constraints.*;

public record DetalleBajaRequest(@NotNull @Positive Long prendaId, @NotNull @Positive Integer cantidad) {}
