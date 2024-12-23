package pe.edu.cibertec.ProyectoDAWI.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pe.edu.cibertec.ProyectoDAWI.entity.Opcion;
import pe.edu.cibertec.ProyectoDAWI.entity.Rol;

import java.util.List;

public interface RolRepository extends CrudRepository<Rol, Integer> {


}
