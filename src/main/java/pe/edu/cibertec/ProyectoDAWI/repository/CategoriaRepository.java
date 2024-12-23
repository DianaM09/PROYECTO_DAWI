package pe.edu.cibertec.ProyectoDAWI.repository;

import org.springframework.data.repository.CrudRepository;
import pe.edu.cibertec.ProyectoDAWI.entity.Categoria;


public interface CategoriaRepository extends CrudRepository<Categoria, Integer> {
    // Aquí puedes agregar consultas personalizadas si es necesario
}
