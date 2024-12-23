package pe.edu.cibertec.ProyectoDAWI.repository;


import org.springframework.data.repository.CrudRepository;
import pe.edu.cibertec.ProyectoDAWI.entity.Producto;


public interface ProductoRepository extends CrudRepository<Producto, Integer> {
    // Puedes agregar consultas personalizadas si es necesario
}
