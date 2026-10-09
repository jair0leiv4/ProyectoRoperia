package cl.duoc.ropaclinica.bajas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "registros_baja")
public class RegistroBaja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MotivoBaja motivo;

    @Column(length = 255)
    private String observacion;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, length = 50)
    private String registradoPor;

    // Un registro tiene muchos detalles; si se borra el registro se borran sus detalles.
    @OneToMany(mappedBy = "registroBaja", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleBaja> detalles = new ArrayList<>();

    public RegistroBaja() {}

    public Long getId() { return id; }
    public MotivoBaja getMotivo() { return motivo; }
    public void setMotivo(MotivoBaja valor) { this.motivo = valor; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String valor) { this.observacion = valor; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime valor) { this.fecha = valor; }
    public String getRegistradoPor() { return registradoPor; }
    public void setRegistradoPor(String valor) { this.registradoPor = valor; }
    public List<DetalleBaja> getDetalles() { return detalles; }

    public void agregarDetalle(DetalleBaja detalle) {
        detalles.add(detalle);
        detalle.setRegistroBaja(this);
    }

    public void limpiarDetalles() {
        detalles.clear();
    }
}
