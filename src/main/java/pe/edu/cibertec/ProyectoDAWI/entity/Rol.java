package pe.edu.cibertec.ProyectoDAWI.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_rol;

    @Column(nullable = false, unique = true)
    private String nombre; // Valores posibles: "ADMINISTRADOR", "CLIENTE"

    @ManyToMany(mappedBy = "roles")
    private List<Usuario> usuarios;
}
