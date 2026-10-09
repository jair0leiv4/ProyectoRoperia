package cl.duoc.ropaclinica.bajas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_baja")
public class DetalleBaja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muchos detalles pertenecen a un registro.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registroBaja_id")
    private RegistroBaja registroBaja;

    @Column(name = "prenda_id", nullable = false)
    private Long prendaId;

    @Column(nullable = false)
    private Integer cantidad;

    public Long getId() { return id; }
    public RegistroBaja getRegistroBaja() { return registroBaja; }
    public void setRegistroBaja(RegistroBaja valor) { this.registroBaja = valor; }
    public Long getPrendaId() { return prendaId; }
    public void setPrendaId(Long valor) { this.prendaId = valor; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer valor) { this.cantidad = valor; }
}
