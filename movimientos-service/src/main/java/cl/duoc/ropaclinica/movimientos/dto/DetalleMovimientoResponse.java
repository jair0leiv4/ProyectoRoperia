package cl.duoc.ropaclinica.movimientos.dto;

import cl.duoc.ropaclinica.movimientos.model.DetalleMovimiento;

public record DetalleMovimientoResponse(Long id, Long prendaId, Integer cantidad) {
    public static DetalleMovimientoResponse de(DetalleMovimiento d) {
        return new DetalleMovimientoResponse(d.getId(), d.getPrendaId(), d.getCantidad());
    }
}
