package cl.duoc.ropaclinica.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TipoPrendaRequest(@NotBlank @Size(max = 50) String nombre, @Size(max = 255) String descripcion) {}
