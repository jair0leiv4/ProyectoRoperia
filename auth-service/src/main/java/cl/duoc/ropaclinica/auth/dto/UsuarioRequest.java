package cl.duoc.ropaclinica.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank @Size(min = 3, max = 50) String username,
        @NotBlank @Size(min = 6, max = 100) String password,
        @NotBlank @Pattern(regexp = "ADMIN|BODEGA|DIGITADOR", message = "rol debe ser ADMIN, BODEGA o DIGITADOR") String rol) {}
