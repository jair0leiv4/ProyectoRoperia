package cl.duoc.ropaclinica.bajas.service;

import cl.duoc.ropaclinica.bajas.dto.DetalleBajaRequest;
import cl.duoc.ropaclinica.bajas.dto.RegistroBajaRequest;
import cl.duoc.ropaclinica.bajas.dto.RegistroBajaResponse;
import cl.duoc.ropaclinica.bajas.model.DetalleBaja;
import cl.duoc.ropaclinica.bajas.model.MotivoBaja;
import cl.duoc.ropaclinica.bajas.model.RegistroBaja;
import cl.duoc.ropaclinica.bajas.repository.RegistroBajaRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class RegistroBajaService {
    private final RegistroBajaRepository repository;

    public RegistroBajaService(RegistroBajaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<RegistroBajaResponse> listar(MotivoBaja filtro) {
        List<RegistroBaja> lista = (filtro == null) ? repository.findAll() : repository.findByMotivo(filtro);
        return lista.stream().map(RegistroBajaResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public RegistroBajaResponse buscar(Long id) {
        return RegistroBajaResponse.de(obtener(id));
    }

    public RegistroBajaResponse crear(RegistroBajaRequest datos, String actor) {
        RegistroBaja e = new RegistroBaja();
        aplicar(e, datos);
        e.setFecha(LocalDateTime.now());
        e.setRegistradoPor(actor);
        return RegistroBajaResponse.de(repository.save(e));
    }

    public RegistroBajaResponse actualizar(Long id, RegistroBajaRequest datos) {
        RegistroBaja e = obtener(id);
        aplicar(e, datos);
        return RegistroBajaResponse.de(repository.save(e));
    }

    public void eliminar(Long id) {
        repository.delete(obtener(id));
    }

    private RegistroBaja obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro no encontrado"));
    }

    // Copia los datos del request a la entidad y reemplaza sus detalles.
    private void aplicar(RegistroBaja e, RegistroBajaRequest d) {
        e.setMotivo(d.motivo());
        e.setObservacion(d.observacion());
        e.limpiarDetalles();
        for (DetalleBajaRequest x : d.detalles()) {
            DetalleBaja det = new DetalleBaja();
            det.setPrendaId(x.prendaId());
            det.setCantidad(x.cantidad());
            e.agregarDetalle(det);
        }
    }
}
