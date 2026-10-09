package cl.duoc.ropaclinica.movimientos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movimientos")
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimiento tipo;

    @Column(nullable = false, length = 100)
    private String servicio;

    @Column(length = 255)
    private String observacion;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, length = 50)
    private String responsable;

    // Un registro tiene muchos detalles; si se borra el registro se borran sus detalles.
    @OneToMany(mappedBy = "movimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleMovimiento> detalles = new ArrayList<>();

    public Movimiento() {}

    public Long getId() { return id; }
    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento valor) { this.tipo = valor; }
    public String getServicio() { return servicio; }
    public void setServicio(String valor) { this.servicio = valor; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String valor) { this.observacion = valor; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime valor) { this.fecha = valor; }
    public String getResponsable() { return responsable; }
    public void setResponsable(String valor) { this.responsable = valor; }
    public List<DetalleMovimiento> getDetalles() { return detalles; }

    public void agregarDetalle(DetalleMovimiento detalle) {
        detalles.add(detalle);
        detalle.setMovimiento(this);
    }

    public void limpiarDetalles() {
        detalles.clear();
    }
}
