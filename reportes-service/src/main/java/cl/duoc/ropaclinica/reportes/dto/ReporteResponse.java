package cl.duoc.ropaclinica.reportes.dto;

import cl.duoc.ropaclinica.reportes.model.TipoReporte;
import cl.duoc.ropaclinica.reportes.model.Reporte;
import java.time.LocalDateTime;
import java.util.List;

public record ReporteResponse(Long id, TipoReporte tipo, String titulo, String resumen, LocalDateTime fecha, String generadoPor,
                          List<ReporteDetalleResponse> detalles) {
    public static ReporteResponse de(Reporte e) {
        return new ReporteResponse(e.getId(), e.getTipo(), e.getTitulo(), e.getResumen(), e.getFecha(), e.getGeneradoPor(),
                e.getDetalles().stream().map(ReporteDetalleResponse::de).toList());
    }
}
