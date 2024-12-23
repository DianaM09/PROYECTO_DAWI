package pe.edu.cibertec.ProyectoDAWI.entity;


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
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_venta;

    @ManyToOne
    @JoinColumn(name = "id_usuario_registro")
    private Usuario usuarioRegistro;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    private Double total;
    private int estado;
    // Relación Many-to-Many con Producto
    @ManyToMany(mappedBy = "ventas") // Relación inversa, se mapea con el atributo "ventas" en Producto
    private List<Producto> productos; // Lista de productos asociados a esta venta
}
