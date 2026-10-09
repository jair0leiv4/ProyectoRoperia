package cl.duoc.ropaclinica.inventario.dto;

import cl.duoc.ropaclinica.inventario.model.TipoPrenda;

public record TipoPrendaResponse(Long id, String nombre, String descripcion) {
    public static TipoPrendaResponse de(TipoPrenda t) {
        return new TipoPrendaResponse(t.getId(), t.getNombre(), t.getDescripcion());
    }
}
