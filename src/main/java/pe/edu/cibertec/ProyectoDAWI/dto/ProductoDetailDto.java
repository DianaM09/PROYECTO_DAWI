package pe.edu.cibertec.ProyectoDAWI.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import pe.edu.cibertec.ProyectoDAWI.entity.Categoria;
import pe.edu.cibertec.ProyectoDAWI.entity.Usuario;

import java.util.Date;

@Data
public class ProductoDetailDto {
    private Integer id_producto;

    private String codigo;

    private String descripcion;

    @ManyToOne  // Relación con la entidad Categoria
    @JoinColumn(name = "id_categoria")  // Nombre de la columna en la base de datos
    private Categoria categoria;

    private Double precio;

    private Integer stock;

    @ManyToOne  // Relación con la entidad Usuario
    @JoinColumn(name = "id_usuario_registro")  // Nombre de la columna en la base de datos
    private Usuario usuarioRegistro;

    private Date fechaRegistro;

    private Date fechaModificacion;

    private int estado;
}
