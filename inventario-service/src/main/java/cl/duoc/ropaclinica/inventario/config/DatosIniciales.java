package cl.duoc.ropaclinica.inventario.config;

import cl.duoc.ropaclinica.inventario.model.TipoPrenda;
import cl.duoc.ropaclinica.inventario.repository.TipoPrendaRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Carga tipos de prenda de ejemplo si la tabla está vacía. */
@Configuration
public class DatosIniciales {
    @Bean
    CommandLineRunner cargarTipos(TipoPrendaRepository tipos) {
        return args -> {
            if (tipos.count() == 0) {
                tipos.saveAll(List.of(
                        new TipoPrenda("Sábana", "Sábana de cama clínica"),
                        new TipoPrenda("Cubrecama", "Cubrecama hospitalario"),
                        new TipoPrenda("Camisa", "Camisa de paciente"),
                        new TipoPrenda("Pijama", "Pijama quirúrgico"),
                        new TipoPrenda("Delantal", "Delantal clínico")));
            }
        };
    }
}
