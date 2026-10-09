package cl.duoc.ropaclinica.bajas.dto;

import cl.duoc.ropaclinica.bajas.model.DetalleBaja;

public record DetalleBajaResponse(Long id, Long prendaId, Integer cantidad) {
    public static DetalleBajaResponse de(DetalleBaja d) {
        return new DetalleBajaResponse(d.getId(), d.getPrendaId(), d.getCantidad());
    }
}
