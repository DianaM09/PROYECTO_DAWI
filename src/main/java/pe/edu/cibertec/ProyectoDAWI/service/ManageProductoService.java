package pe.edu.cibertec.ProyectoDAWI.service;

import pe.edu.cibertec.ProyectoDAWI.dto.CategoriaDto;
import pe.edu.cibertec.ProyectoDAWI.dto.ProductoCreateDto;
import pe.edu.cibertec.ProyectoDAWI.dto.ProductoDetailDto;
import pe.edu.cibertec.ProyectoDAWI.dto.ProductoDto;

import java.util.List;
import java.util.Optional;

public interface ManageProductoService {

    List<ProductoDto> getAllProductos();
    Optional<ProductoDetailDto> getDetailProducto(Integer id_producto);
    boolean updateProducto(ProductoDto productoDto);
    boolean deleteProducto(Integer id_producto);
    boolean createProducto(ProductoCreateDto productoCreateDto);
    List<CategoriaDto> getAllCategorias();

}