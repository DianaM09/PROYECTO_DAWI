package pe.edu.cibertec.ProyectoDAWI.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pe.edu.cibertec.ProyectoDAWI.entity.Venta;

import java.util.Optional;


public interface VentaRepository extends CrudRepository<Venta, Integer> {
    @Query("SELECT v FROM Venta v JOIN FETCH v.productos WHERE v.id_venta = :id_venta")
    Optional<Venta> findVentaWithProductos(@Param("id_venta") Integer id_venta);

    // Aquí puedes agregar consultas personalizadas si es necesario
}
