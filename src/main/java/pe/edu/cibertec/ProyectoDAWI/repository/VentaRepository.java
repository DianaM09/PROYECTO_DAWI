package pe.edu.cibertec.ProyectoDAWI.repository;


import org.springframework.data.repository.CrudRepository;
import pe.edu.cibertec.ProyectoDAWI.entity.Venta;


public interface VentaRepository extends CrudRepository<Venta, Integer> {
    // Aquí puedes agregar consultas personalizadas si es necesario
}
