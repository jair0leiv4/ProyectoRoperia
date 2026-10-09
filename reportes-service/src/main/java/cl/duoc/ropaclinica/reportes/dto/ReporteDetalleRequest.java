package cl.duoc.ropaclinica.reportes.dto;

import jakarta.validation.constraints.*;

public record ReporteDetalleRequest(@NotBlank @Size(max = 100) String concepto, @NotNull @Min(0) Integer valor) {}
