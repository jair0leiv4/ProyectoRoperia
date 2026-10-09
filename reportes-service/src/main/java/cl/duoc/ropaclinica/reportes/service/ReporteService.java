package cl.duoc.ropaclinica.reportes.service;

import cl.duoc.ropaclinica.reportes.dto.ReporteDetalleRequest;
import cl.duoc.ropaclinica.reportes.dto.ReporteRequest;
import cl.duoc.ropaclinica.reportes.dto.ReporteResponse;
import cl.duoc.ropaclinica.reportes.model.ReporteDetalle;
import cl.duoc.ropaclinica.reportes.model.TipoReporte;
import cl.duoc.ropaclinica.reportes.model.Reporte;
import cl.duoc.ropaclinica.reportes.repository.ReporteRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ReporteService {
    private final ReporteRepository repository;

    public ReporteService(ReporteRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ReporteResponse> listar(TipoReporte filtro) {
        List<Reporte> lista = (filtro == null) ? repository.findAll() : repository.findByTipo(filtro);
        return lista.stream().map(ReporteResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public ReporteResponse buscar(Long id) {
        return ReporteResponse.de(obtener(id));
    }

    public ReporteResponse crear(ReporteRequest datos, String actor) {
        Reporte e = new Reporte();
        aplicar(e, datos);
        e.setFecha(LocalDateTime.now());
        e.setGeneradoPor(actor);
        return ReporteResponse.de(repository.save(e));
    }

    public ReporteResponse actualizar(Long id, ReporteRequest datos) {
        Reporte e = obtener(id);
        aplicar(e, datos);
        return ReporteResponse.de(repository.save(e));
    }

    public void eliminar(Long id) {
        repository.delete(obtener(id));
    }

    private Reporte obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro no encontrado"));
    }

    // Copia los datos del request a la entidad y reemplaza sus detalles.
    private void aplicar(Reporte e, ReporteRequest d) {
        e.setTipo(d.tipo());
        e.setTitulo(d.titulo());
        e.setResumen(d.resumen());
        e.limpiarDetalles();
        for (ReporteDetalleRequest x : d.detalles()) {
            ReporteDetalle det = new ReporteDetalle();
            det.setConcepto(x.concepto());
            det.setValor(x.valor());
            e.agregarDetalle(det);
        }
    }
}
