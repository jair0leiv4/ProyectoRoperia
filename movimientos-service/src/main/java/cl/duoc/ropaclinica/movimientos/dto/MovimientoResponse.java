package cl.duoc.ropaclinica.movimientos.dto;

import cl.duoc.ropaclinica.movimientos.model.TipoMovimiento;
import cl.duoc.ropaclinica.movimientos.model.Movimiento;
import java.time.LocalDateTime;
import java.util.List;

public record MovimientoResponse(Long id, TipoMovimiento tipo, String servicio, String observacion, LocalDateTime fecha, String responsable,
                          List<DetalleMovimientoResponse> detalles) {
    public static MovimientoResponse de(Movimiento e) {
        return new MovimientoResponse(e.getId(), e.getTipo(), e.getServicio(), e.getObservacion(), e.getFecha(), e.getResponsable(),
                e.getDetalles().stream().map(DetalleMovimientoResponse::de).toList());
    }
}
