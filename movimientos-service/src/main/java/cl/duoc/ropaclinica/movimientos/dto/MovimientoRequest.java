package cl.duoc.ropaclinica.movimientos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import cl.duoc.ropaclinica.movimientos.model.TipoMovimiento;
import java.util.List;

public record MovimientoRequest(
        @NotNull TipoMovimiento tipo,
        @NotBlank @Size(max = 100) String servicio,
        @Size(max = 255) String observacion,
        @NotEmpty @Valid List<DetalleMovimientoRequest> detalles) {}
