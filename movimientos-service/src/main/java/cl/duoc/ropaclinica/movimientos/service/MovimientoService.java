package cl.duoc.ropaclinica.movimientos.service;

import cl.duoc.ropaclinica.movimientos.dto.DetalleMovimientoRequest;
import cl.duoc.ropaclinica.movimientos.dto.MovimientoRequest;
import cl.duoc.ropaclinica.movimientos.dto.MovimientoResponse;
import cl.duoc.ropaclinica.movimientos.model.DetalleMovimiento;
import cl.duoc.ropaclinica.movimientos.model.TipoMovimiento;
import cl.duoc.ropaclinica.movimientos.model.Movimiento;
import cl.duoc.ropaclinica.movimientos.repository.MovimientoRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class MovimientoService {
    private final MovimientoRepository repository;

    public MovimientoService(MovimientoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponse> listar(TipoMovimiento filtro) {
        List<Movimiento> lista = (filtro == null) ? repository.findAll() : repository.findByTipo(filtro);
        return lista.stream().map(MovimientoResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public MovimientoResponse buscar(Long id) {
        return MovimientoResponse.de(obtener(id));
    }

    public MovimientoResponse crear(MovimientoRequest datos, String actor) {
        Movimiento e = new Movimiento();
        aplicar(e, datos);
        e.setFecha(LocalDateTime.now());
        e.setResponsable(actor);
        return MovimientoResponse.de(repository.save(e));
    }

    public MovimientoResponse actualizar(Long id, MovimientoRequest datos) {
        Movimiento e = obtener(id);
        aplicar(e, datos);
        return MovimientoResponse.de(repository.save(e));
    }

    public void eliminar(Long id) {
        repository.delete(obtener(id));
    }

    private Movimiento obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro no encontrado"));
    }

    // Copia los datos del request a la entidad y reemplaza sus detalles.
    private void aplicar(Movimiento e, MovimientoRequest d) {
        e.setTipo(d.tipo());
        e.setServicio(d.servicio());
        e.setObservacion(d.observacion());
        e.limpiarDetalles();
        for (DetalleMovimientoRequest x : d.detalles()) {
            DetalleMovimiento det = new DetalleMovimiento();
            det.setPrendaId(x.prendaId());
            det.setCantidad(x.cantidad());
            e.agregarDetalle(det);
        }
    }
}
