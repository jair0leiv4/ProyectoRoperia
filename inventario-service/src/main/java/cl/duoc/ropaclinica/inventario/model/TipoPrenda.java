package cl.duoc.ropaclinica.inventario.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tipos_prenda")
public class TipoPrenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre; // Sábana, Cubrecama, Camisa...

    @Column(length = 255)
    private String descripcion;

    // Un tipo de prenda tiene muchas prendas en stock (lado inverso de la relación).
    @OneToMany(mappedBy = "tipoPrenda")
    private List<PrendaStock> prendas = new ArrayList<>();

    public TipoPrenda() {}

    public TipoPrenda(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String valor) { this.nombre = valor; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String valor) { this.descripcion = valor; }
    public List<PrendaStock> getPrendas() { return prendas; }
}
