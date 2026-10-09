package cl.duoc.ropaclinica.bajas.repository;

import cl.duoc.ropaclinica.bajas.model.MotivoBaja;
import cl.duoc.ropaclinica.bajas.model.RegistroBaja;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistroBajaRepository extends JpaRepository<RegistroBaja, Long> {
    List<RegistroBaja> findByMotivo(MotivoBaja motivo);
}
