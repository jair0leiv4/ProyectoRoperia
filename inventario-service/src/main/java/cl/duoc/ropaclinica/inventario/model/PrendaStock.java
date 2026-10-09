package cl.duoc.ropaclinica.inventario.model;

import jakarta.persistence.*;

@Entity
@Table(name = "prendas_stock")
public class PrendaStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // null antes de guardar; JPA asigna el identificador.

    // Muchas prendas en stock pertenecen a un tipo de prenda.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_prenda_id")
    private TipoPrenda tipoPrenda;

    @Column(nullable = false, length = 20)
    private String talla; // S, M, L, XL o UNICA

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo;

    @Column(nullable = false, length = 100)
    private String ubicacion; // estante o sector de la ropería

    public PrendaStock() {}

    public PrendaStock(TipoPrenda tipoPrenda, String talla, Integer cantidad, Integer stockMinimo, String ubicacion) {
        this.tipoPrenda = tipoPrenda;
        this.talla = talla;
        this.cantidad = cantidad;
        this.stockMinimo = stockMinimo;
        this.ubicacion = ubicacion;
    }

    public Long getId() { return id; }
    public TipoPrenda getTipoPrenda() { return tipoPrenda; }
    public void setTipoPrenda(TipoPrenda valor) { this.tipoPrenda = valor; }
    public String getTalla() { return talla; }
    public void setTalla(String valor) { this.talla = valor; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer valor) { this.cantidad = valor; }
    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer valor) { this.stockMinimo = valor; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String valor) { this.ubicacion = valor; }
}
