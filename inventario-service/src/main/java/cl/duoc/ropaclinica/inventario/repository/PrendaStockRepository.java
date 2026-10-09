package cl.duoc.ropaclinica.inventario.repository;

import cl.duoc.ropaclinica.inventario.model.PrendaStock;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PrendaStockRepository extends JpaRepository<PrendaStock, Long> {
    @Override
    @EntityGraph(attributePaths = "tipoPrenda")
    List<PrendaStock> findAll();

    @Override
    @EntityGraph(attributePaths = "tipoPrenda")
    Optional<PrendaStock> findById(Long id);

    @EntityGraph(attributePaths = "tipoPrenda")
    List<PrendaStock> findByTipoPrendaId(Long tipoPrendaId);

    // Prendas cuya cantidad llegó al mínimo definido (alerta de poco stock).
    @Query("select p from PrendaStock p join fetch p.tipoPrenda where p.cantidad <= p.stockMinimo")
    List<PrendaStock> buscarBajoStock();
}
