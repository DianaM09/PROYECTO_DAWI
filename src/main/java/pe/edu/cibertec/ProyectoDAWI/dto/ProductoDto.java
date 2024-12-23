package pe.edu.cibertec.ProyectoDAWI.dto;


import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import pe.edu.cibertec.ProyectoDAWI.entity.Categoria;

@Data
public class ProductoDto {
    private Integer id_producto;
    private String codigo;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private String categoriaDescripcion;  // Campo para almacenar la descripción de la categoría
}
