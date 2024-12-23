package pe.edu.cibertec.ProyectoDAWI.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class ProductoCreateDto {
    private String codigo;
    private String descripcion;
    private Integer id_categoria; // ID de la categoría seleccionada
    private Double precio;
    private Integer stock;
    private Integer id_usuario_registro; // ID del usuario que registra el producto
}
