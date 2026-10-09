package cl.duoc.ropaclinica.inventario.service;

import cl.duoc.ropaclinica.inventario.dto.TipoPrendaRequest;
import cl.duoc.ropaclinica.inventario.dto.TipoPrendaResponse;
import cl.duoc.ropaclinica.inventario.model.TipoPrenda;
import cl.duoc.ropaclinica.inventario.repository.TipoPrendaRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class TipoPrendaService {
    private final TipoPrendaRepository repository;

    public TipoPrendaService(TipoPrendaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<TipoPrendaResponse> listar() {
        return repository.findAll().stream().map(TipoPrendaResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public TipoPrendaResponse buscar(Long id) {
        return TipoPrendaResponse.de(obtener(id));
    }

    public TipoPrendaResponse crear(TipoPrendaRequest datos) {
        if (repository.existsByNombreIgnoreCase(datos.nombre())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El tipo de prenda ya existe");
        }
        return TipoPrendaResponse.de(repository.save(new TipoPrenda(datos.nombre(), datos.descripcion())));
    }

    public TipoPrendaResponse actualizar(Long id, TipoPrendaRequest datos) {
        TipoPrenda actual = obtener(id);
        actual.setNombre(datos.nombre());
        actual.setDescripcion(datos.descripcion());
        return TipoPrendaResponse.de(repository.save(actual));
    }

    public void eliminar(Long id) {
        TipoPrenda tipo = obtener(id);
        if (!tipo.getPrendas().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El tipo tiene prendas asociadas");
        }
        repository.delete(tipo);
    }

    private TipoPrenda obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro no encontrado"));
    }
}
