package cl.duoc.ropaclinica.reportes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "reporte_detalle")
public class ReporteDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muchos detalles pertenecen a un registro.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reporte_id")
    private Reporte reporte;

    @Column(nullable = false, length = 100)
    private String concepto;

    @Column(nullable = false)
    private Integer valor;

    public Long getId() { return id; }
    public Reporte getReporte() { return reporte; }
    public void setReporte(Reporte valor) { this.reporte = valor; }
    public String getConcepto() { return concepto; }
    public void setConcepto(String valor) { this.concepto = valor; }
    public Integer getValor() { return valor; }
    public void setValor(Integer valor) { this.valor = valor; }
}
