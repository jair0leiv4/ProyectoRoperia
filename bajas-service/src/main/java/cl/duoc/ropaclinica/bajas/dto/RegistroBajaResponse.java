package cl.duoc.ropaclinica.bajas.dto;

import cl.duoc.ropaclinica.bajas.model.MotivoBaja;
import cl.duoc.ropaclinica.bajas.model.RegistroBaja;
import java.time.LocalDateTime;
import java.util.List;

public record RegistroBajaResponse(Long id, MotivoBaja motivo, String observacion, LocalDateTime fecha, String registradoPor,
                          List<DetalleBajaResponse> detalles) {
    public static RegistroBajaResponse de(RegistroBaja e) {
        return new RegistroBajaResponse(e.getId(), e.getMotivo(), e.getObservacion(), e.getFecha(), e.getRegistradoPor(),
                e.getDetalles().stream().map(DetalleBajaResponse::de).toList());
    }
}
