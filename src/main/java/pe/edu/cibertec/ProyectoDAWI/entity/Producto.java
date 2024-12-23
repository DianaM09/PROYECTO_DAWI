package pe.edu.cibertec.ProyectoDAWI.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
    private String foto;

    @Transient // Usamos @Transient si la cantidad no se guarda directamente en la base de datos
    private int cantidad;
    // Relación Many-to-Many con Producto
    // Relación Many-to-Many con Venta
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)

    @JoinTable(
            name = "venta_producto", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "id_producto"), // Columna que hace referencia a Producto
            inverseJoinColumns = @JoinColumn(name = "id_venta") // Columna que hace referencia a Venta
    )
    @JsonIgnore
    private List<Venta> ventas; // Lista de ventas asociadas a este producto
}

