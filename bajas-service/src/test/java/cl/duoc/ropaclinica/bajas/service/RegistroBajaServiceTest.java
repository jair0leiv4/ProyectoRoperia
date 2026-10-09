package cl.duoc.ropaclinica.bajas.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import cl.duoc.ropaclinica.bajas.dto.DetalleBajaRequest;
import cl.duoc.ropaclinica.bajas.dto.RegistroBajaRequest;
import cl.duoc.ropaclinica.bajas.dto.RegistroBajaResponse;
import cl.duoc.ropaclinica.bajas.model.MotivoBaja;
import cl.duoc.ropaclinica.bajas.model.RegistroBaja;
import cl.duoc.ropaclinica.bajas.repository.RegistroBajaRepository;
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
class RegistroBajaServiceTest {
    @Mock RegistroBajaRepository repository;
    @InjectMocks RegistroBajaService service;

    @Test
    void crearGuardaElRegistroConSusDetallesYElAutor() {
        when(repository.save(any(RegistroBaja.class))).thenAnswer(i -> i.getArgument(0));
        RegistroBajaRequest request = new RegistroBajaRequest(MotivoBaja.DETERIORO, null, List.of(new DetalleBajaRequest(1L, 2)));

        RegistroBajaResponse resp = service.crear(request, "tester");

        assertEquals("tester", resp.registradoPor());
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
        when(repository.findAll()).thenReturn(List.of(new RegistroBaja()));
        // La entidad vacía no tiene fecha ni detalles nulos: basta con comprobar que se consultó todo.
        assertDoesNotThrow(() -> service.listar(null));
        verify(repository).findAll();
    }
}
