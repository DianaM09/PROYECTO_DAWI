package pe.edu.cibertec.ProyectoDAWI.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_producto;
    private String codigo;
    private String descripcion;
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
    private Double precio;
    private Integer stock;
    @ManyToOne
    @JoinColumn(name = "id_usuario_registro")
    private Usuario usuarioRegistro;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    private int estado;

    @Transient // Usamos @Transient si la cantidad no se guarda directamente en la base de datos
    private int cantidad;
}

