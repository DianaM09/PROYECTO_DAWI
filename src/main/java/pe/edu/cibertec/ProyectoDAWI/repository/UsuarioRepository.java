package pe.edu.cibertec.ProyectoDAWI.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pe.edu.cibertec.ProyectoDAWI.entity.Opcion;
import pe.edu.cibertec.ProyectoDAWI.entity.Rol;
import pe.edu.cibertec.ProyectoDAWI.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);

}

