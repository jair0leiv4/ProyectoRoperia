package cl.duoc.ropaclinica.reportes.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reportes")
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoReporte tipo;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(length = 500)
    private String resumen;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, length = 50)
    private String generadoPor;

    // Un registro tiene muchos detalles; si se borra el registro se borran sus detalles.
    @OneToMany(mappedBy = "reporte", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReporteDetalle> detalles = new ArrayList<>();

    public Reporte() {}

    public Long getId() { return id; }
    public TipoReporte getTipo() { return tipo; }
    public void setTipo(TipoReporte valor) { this.tipo = valor; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String valor) { this.titulo = valor; }
    public String getResumen() { return resumen; }
    public void setResumen(String valor) { this.resumen = valor; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime valor) { this.fecha = valor; }
    public String getGeneradoPor() { return generadoPor; }
    public void setGeneradoPor(String valor) { this.generadoPor = valor; }
    public List<ReporteDetalle> getDetalles() { return detalles; }

    public void agregarDetalle(ReporteDetalle detalle) {
        detalles.add(detalle);
        detalle.setReporte(this);
    }

    public void limpiarDetalles() {
        detalles.clear();
    }
}
