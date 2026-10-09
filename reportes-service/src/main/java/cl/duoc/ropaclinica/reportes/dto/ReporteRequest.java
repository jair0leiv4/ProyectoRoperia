package cl.duoc.ropaclinica.reportes.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import cl.duoc.ropaclinica.reportes.model.TipoReporte;
import java.util.List;

public record ReporteRequest(
        @NotNull TipoReporte tipo,
        @NotBlank @Size(max = 150) String titulo,
        @Size(max = 500) String resumen,
        @NotEmpty @Valid List<ReporteDetalleRequest> detalles) {}
