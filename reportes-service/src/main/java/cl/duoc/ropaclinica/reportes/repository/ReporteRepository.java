package cl.duoc.ropaclinica.reportes.repository;

import cl.duoc.ropaclinica.reportes.model.TipoReporte;
import cl.duoc.ropaclinica.reportes.model.Reporte;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByTipo(TipoReporte tipo);
}
