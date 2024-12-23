package pe.edu.cibertec.ProyectoDAWI.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta_Has_ProductoPK implements Serializable  {
    private Integer id_venta;
    private Integer id_producto;
}
