package cl.duoc.ropaclinica.inventario.dto;

import cl.duoc.ropaclinica.inventario.model.PrendaStock;

public record PrendaResponse(Long id, Long tipoPrendaId, String tipo, String talla, Integer cantidad,
                             Integer stockMinimo, String ubicacion) {
    public static PrendaResponse de(PrendaStock p) {
        return new PrendaResponse(p.getId(), p.getTipoPrenda().getId(), p.getTipoPrenda().getNombre(),
                p.getTalla(), p.getCantidad(), p.getStockMinimo(), p.getUbicacion());
    }
}
