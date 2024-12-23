package pe.edu.cibertec.ProyectoDAWI.repository;


import org.springframework.data.repository.CrudRepository;
import pe.edu.cibertec.ProyectoDAWI.entity.Venta_Has_Producto;
import pe.edu.cibertec.ProyectoDAWI.entity.Venta_Has_ProductoPK;


public interface VentaHasProductoRepository extends CrudRepository<Venta_Has_Producto, Venta_Has_ProductoPK> {
    // Puedes agregar consultas personalizadas si es necesario
}
