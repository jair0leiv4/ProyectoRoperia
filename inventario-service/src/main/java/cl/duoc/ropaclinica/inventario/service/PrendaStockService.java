package cl.duoc.ropaclinica.inventario.service;

import cl.duoc.ropaclinica.inventario.dto.PrendaRequest;
import cl.duoc.ropaclinica.inventario.dto.PrendaResponse;
import cl.duoc.ropaclinica.inventario.model.PrendaStock;
import cl.duoc.ropaclinica.inventario.model.TipoPrenda;
import cl.duoc.ropaclinica.inventario.repository.PrendaStockRepository;
import cl.duoc.ropaclinica.inventario.repository.TipoPrendaRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class PrendaStockService {
    private final PrendaStockRepository repository;
    private final TipoPrendaRepository tipoRepository;

    public PrendaStockService(PrendaStockRepository repository, TipoPrendaRepository tipoRepository) {
        this.repository = repository;
        this.tipoRepository = tipoRepository;
    }

    @Transactional(readOnly = true)
    public List<PrendaResponse> listar(Long tipoPrendaId) {
        List<PrendaStock> lista = (tipoPrendaId == null) ? repository.findAll() : repository.findByTipoPrendaId(tipoPrendaId);
        return lista.stream().map(PrendaResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public List<PrendaResponse> bajoStock() {
        return repository.buscarBajoStock().stream().map(PrendaResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public PrendaResponse buscar(Long id) {
        return PrendaResponse.de(obtener(id));
    }

    public PrendaResponse crear(PrendaRequest datos) {
        PrendaStock prenda = new PrendaStock(buscarTipo(datos.tipoPrendaId()), datos.talla(),
                datos.cantidad(), datos.stockMinimo(), datos.ubicacion());
        return PrendaResponse.de(repository.save(prenda));
    }

    public PrendaResponse actualizar(Long id, PrendaRequest datos) {
        PrendaStock actual = obtener(id);
        actual.setTipoPrenda(buscarTipo(datos.tipoPrendaId()));
        actual.setTalla(datos.talla());
        actual.setCantidad(datos.cantidad());
        actual.setStockMinimo(datos.stockMinimo());
        actual.setUbicacion(datos.ubicacion());
        return PrendaResponse.de(repository.save(actual));
    }

    public void eliminar(Long id) {
        repository.delete(obtener(id));
    }

    private PrendaStock obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro no encontrado"));
    }

    private TipoPrenda buscarTipo(Long id) {
        return tipoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de prenda no encontrado"));
    }
}
