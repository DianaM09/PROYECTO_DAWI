package pe.edu.cibertec.ProyectoDAWI.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonIgnore

    private List<Usuario> usuarios;
}
