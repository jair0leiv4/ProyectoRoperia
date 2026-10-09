package cl.duoc.ropaclinica.auth.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password; // hash BCrypt, nunca texto plano

    // Muchos usuarios pertenecen a un rol.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rol_id")
    private Rol rol;

    public Usuario() {}

    public Usuario(String username, String password, Rol rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String valor) { this.username = valor; }
    public String getPassword() { return password; }
    public void setPassword(String valor) { this.password = valor; }
    public Rol getRol() { return rol; }
    public void setRol(Rol valor) { this.rol = valor; }
}
