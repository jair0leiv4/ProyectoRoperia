package cl.duoc.ropaclinica.movimientos.repository;

import cl.duoc.ropaclinica.movimientos.model.TipoMovimiento;
import cl.duoc.ropaclinica.movimientos.model.Movimiento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByTipo(TipoMovimiento tipo);
}
