package pe.edu.cibertec.ProyectoDAWI.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Opcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_opcion;
    private String nombre;
    private int estado;
    private String ruta;
    private Short tipo;
}