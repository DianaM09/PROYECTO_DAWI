package pe.edu.cibertec.ProyectoDAWI.dto;


import jakarta.persistence.Column;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // Genera constructor con todos los campos
@NoArgsConstructor  // Genera constructor sin argumentos
public class CategoriaDto {
        private Integer id_categoria;
        private String descripcion;
}
