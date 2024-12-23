package pe.edu.cibertec.ProyectoDAWI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.ProyectoDAWI.entity.Producto;
import pe.edu.cibertec.ProyectoDAWI.repository.ProductoRepository;

import java.util.Optional;
@Service
public class CarritoService {

    @Autowired
    private ProductoRepository productoRepository;

    // Método para obtener un producto por su ID
    public Producto obtenerProductoPorId(Integer id_producto) {
        Optional<Producto> productoOptional = productoRepository.findById(id_producto);
        return productoOptional.orElse(null);  // Retorna null si no se encuentra el producto
    }

}
