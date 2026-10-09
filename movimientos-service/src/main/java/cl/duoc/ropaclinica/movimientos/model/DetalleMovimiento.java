package cl.duoc.ropaclinica.movimientos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_movimiento")
public class DetalleMovimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muchos detalles pertenecen a un registro.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movimiento_id")
    private Movimiento movimiento;

    @Column(name = "prenda_id", nullable = false)
    private Long prendaId;

    @Column(nullable = false)
    private Integer cantidad;

    public Long getId() { return id; }
    public Movimiento getMovimiento() { return movimiento; }
    public void setMovimiento(Movimiento valor) { this.movimiento = valor; }
    public Long getPrendaId() { return prendaId; }
    public void setPrendaId(Long valor) { this.prendaId = valor; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer valor) { this.cantidad = valor; }
}
