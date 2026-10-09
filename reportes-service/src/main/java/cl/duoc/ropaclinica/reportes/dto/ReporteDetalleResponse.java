package cl.duoc.ropaclinica.reportes.dto;

import cl.duoc.ropaclinica.reportes.model.ReporteDetalle;

public record ReporteDetalleResponse(Long id, String concepto, Integer valor) {
    public static ReporteDetalleResponse de(ReporteDetalle d) {
        return new ReporteDetalleResponse(d.getId(), d.getConcepto(), d.getValor());
    }
}
