package cl.duoc.ropaclinica.auth.dto;

import cl.duoc.ropaclinica.auth.model.Usuario;

public record UsuarioResponse(Long id, String username, String rol) {
    public static UsuarioResponse de(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getUsername(), u.getRol().getNombre());
    }
}
