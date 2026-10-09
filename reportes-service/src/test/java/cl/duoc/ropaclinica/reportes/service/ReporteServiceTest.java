package cl.duoc.ropaclinica.reportes.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import cl.duoc.ropaclinica.reportes.dto.ReporteDetalleRequest;
import cl.duoc.ropaclinica.reportes.dto.ReporteRequest;
import cl.duoc.ropaclinica.reportes.dto.ReporteResponse;
import cl.duoc.ropaclinica.reportes.model.TipoReporte;
import cl.duoc.ropaclinica.reportes.model.Reporte;
import cl.duoc.ropaclinica.reportes.repository.ReporteRepository;
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
class ReporteServiceTest {
    @Mock ReporteRepository repository;
    @InjectMocks ReporteService service;

    @Test
    void crearGuardaElRegistroConSusDetallesYElAutor() {
        when(repository.save(any(Reporte.class))).thenAnswer(i -> i.getArgument(0));
        ReporteRequest request = new ReporteRequest(TipoReporte.STOCK, "x", null, List.of(new ReporteDetalleRequest("x", 3)));

        ReporteResponse resp = service.crear(request, "tester");

        assertEquals("tester", resp.generadoPor());
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
        when(repository.findAll()).thenReturn(List.of(new Reporte()));
        // La entidad vacía no tiene fecha ni detalles nulos: basta con comprobar que se consultó todo.
        assertDoesNotThrow(() -> service.listar(null));
        verify(repository).findAll();
    }
}
