package cl.duoc.ropaclinica.inventario.repository;

import cl.duoc.ropaclinica.inventario.model.TipoPrenda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoPrendaRepository extends JpaRepository<TipoPrenda, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
}
