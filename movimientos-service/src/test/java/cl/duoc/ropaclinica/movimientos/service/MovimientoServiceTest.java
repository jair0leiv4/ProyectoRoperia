package cl.duoc.ropaclinica.movimientos.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import cl.duoc.ropaclinica.movimientos.dto.DetalleMovimientoRequest;
import cl.duoc.ropaclinica.movimientos.dto.MovimientoRequest;
import cl.duoc.ropaclinica.movimientos.dto.MovimientoResponse;
import cl.duoc.ropaclinica.movimientos.model.TipoMovimiento;
import cl.duoc.ropaclinica.movimientos.model.Movimiento;
import cl.duoc.ropaclinica.movimientos.repository.MovimientoRepository;
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
class MovimientoServiceTest {
    @Mock MovimientoRepository repository;
    @InjectMocks MovimientoService service;

    @Test
    void crearGuardaElRegistroConSusDetallesYElAutor() {
        when(repository.save(any(Movimiento.class))).thenAnswer(i -> i.getArgument(0));
        MovimientoRequest request = new MovimientoRequest(TipoMovimiento.ENTREGA, "x", null, List.of(new DetalleMovimientoRequest(1L, 2)));

        MovimientoResponse resp = service.crear(request, "tester");

        assertEquals("tester", resp.responsable());
        assertEquals(1, resp.detalles().size());
        assertNotNull(resp.fecha());
    }

    @Test
    void buscarInexistenteLanza404() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.buscar(99L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void listarSinFiltroUsaFindAll() {
        when(repository.findAll()).thenReturn(List.of(new Movimiento()));
        // La entidad vacía no tiene fecha ni detalles nulos: basta con comprobar que se consultó todo.
        assertDoesNotThrow(() -> service.listar(null));
        verify(repository).findAll();
    }
}
