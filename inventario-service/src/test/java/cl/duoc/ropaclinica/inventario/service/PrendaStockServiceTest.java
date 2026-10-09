package cl.duoc.ropaclinica.inventario.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import cl.duoc.ropaclinica.inventario.dto.PrendaRequest;
import cl.duoc.ropaclinica.inventario.dto.PrendaResponse;
import cl.duoc.ropaclinica.inventario.model.PrendaStock;
import cl.duoc.ropaclinica.inventario.model.TipoPrenda;
import cl.duoc.ropaclinica.inventario.repository.PrendaStockRepository;
import cl.duoc.ropaclinica.inventario.repository.TipoPrendaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class PrendaStockServiceTest {
    @Mock PrendaStockRepository repository;
    @Mock TipoPrendaRepository tipoRepository;
    @InjectMocks PrendaStockService service;

    @Test
    void crearGuardaLaPrendaConSuTipo() {
        TipoPrenda sabana = new TipoPrenda("Sábana", "Sábana clínica");
        when(tipoRepository.findById(1L)).thenReturn(Optional.of(sabana));
        when(repository.save(any(PrendaStock.class))).thenAnswer(i -> i.getArgument(0));

        PrendaResponse resp = service.crear(new PrendaRequest(1L, "UNICA", 50, 10, "Estante A1"));

        assertEquals("Sábana", resp.tipo());
        assertEquals(50, resp.cantidad());
    }

    @Test
    void crearConTipoInexistenteLanza404() {
        when(tipoRepository.findById(99L)).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.crear(new PrendaRequest(99L, "UNICA", 5, 1, "A1")));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void bajoStockMapeaLosResultadosDelRepositorio() {
        PrendaStock prenda = new PrendaStock(new TipoPrenda("Camisa", null), "M", 2, 5, "B2");
        when(repository.buscarBajoStock()).thenReturn(List.of(prenda));
        assertEquals(1, service.bajoStock().size());
    }
}
