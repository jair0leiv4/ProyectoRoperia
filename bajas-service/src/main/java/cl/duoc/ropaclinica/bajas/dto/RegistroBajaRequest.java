package cl.duoc.ropaclinica.bajas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import cl.duoc.ropaclinica.bajas.model.MotivoBaja;
import java.util.List;

public record RegistroBajaRequest(
        @NotNull MotivoBaja motivo,
        @Size(max = 255) String observacion,
        @NotEmpty @Valid List<DetalleBajaRequest> detalles) {}
